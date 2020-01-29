package com.jyh.excise.io;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Java的IO标准库提供的InputStream根据来源可以包括：
 *
 * FileInputStream：从文件读取数据，是最终数据源；
 * ServletInputStream：从HTTP请求读取数据，是最终数据源；
 * Socket.getInputStream()：从TCP连接读取数据，是最终数据源；
 * ...
 * 如果我们要给FileInputStream添加缓冲功能，则可以从FileInputStream派生一个类：
 *
 * BufferedFileInputStream extends FileInputStream
 * 如果要给FileInputStream添加计算签名的功能，类似的，也可以从FileInputStream派生一个类：
 *
 * DigestFileInputStream extends FileInputStream
 * 如果要给FileInputStream添加加密/解密功能，还是可以从FileInputStream派生一个类：
 *
 * CipherFileInputStream extends FileInputStream
 * 如果要给FileInputStream添加缓冲和签名的功能，那么我们还需要派生BufferedDigestFileInputStream。如果要给FileInputStream添加缓冲和加解密的功能，则需要派生BufferedCipherFileInputStream。
 *
 * 我们发现，给FileInputStream添加3种功能，至少需要3个子类。这3种功能的组合，又需要更多的子类：
 *
 *                           ┌─────────────────┐
 *                           │ FileInputStream │
 *                           └─────────────────┘
 *                                    ▲
 *              ┌───────────┬─────────┼─────────┬───────────┐
 *              │           │         │         │           │
 * ┌───────────────────────┐│┌─────────────────┐│┌─────────────────────┐
 * │BufferedFileInputStream│││DigestInputStream│││CipherFileInputStream│
 * └───────────────────────┘│└─────────────────┘│└─────────────────────┘
 *                          │                   │
 *     ┌─────────────────────────────┐ ┌─────────────────────────────┐
 *     │BufferedDigestFileInputStream│ │BufferedCipherFileInputStream│
 *     └─────────────────────────────┘ └─────────────────────────────┘
 * 这还只是针对FileInputStream设计，如果针对另一种InputStream设计，很快会出现子类爆炸的情况。
 *
 * 因此，直接使用继承，为各种InputStream附加更多的功能，根本无法控制代码的复杂度，很快就会失控。
 *
 * 为了解决依赖继承会导致子类数量失控的问题，JDK首先将InputStream分为两大类：
 *
 * 一类是直接提供数据的基础InputStream，例如：
 *
 * FileInputStream
 * ByteArrayInputStream
 * ServletInputStream
 * ...
 * 一类是提供额外附加功能的InputStream，例如：
 *
 * BufferedInputStream
 * DigestInputStream
 * CipherInputStream
 * ...
 * 当我们需要给一个“基础”InputStream附加各种功能时，我们先确定这个能提供数据源的InputStream，因为我们需要的数据总得来自某个地方，例如，FileInputStream，数据来源自文件：
 *
 * InputStream file = new FileInputStream("test.gz");
 * 紧接着，我们希望FileInputStream能提供缓冲的功能来提高读取的效率，因此我们用BufferedInputStream包装这个InputStream，得到的包装类型是BufferedInputStream，但它仍然被视为一个InputStream：
 *
 * InputStream buffered = new BufferedInputStream(file);
 * 最后，假设该文件已经用gzip压缩了，我们希望直接读取解压缩的内容，就可以再包装一个GZIPInputStream：
 *
 * InputStream gzip = new GZIPInputStream(buffered);
 * 无论我们包装多少次，得到的对象始终是InputStream，我们直接用InputStream来引用它，就可以正常读取：
 *
 * ┌─────────────────────────┐
 * │GZIPInputStream          │
 * │┌───────────────────────┐│
 * ││BufferedFileInputStream││
 * ││┌─────────────────────┐││
 * │││   FileInputStream   │││
 * ││└─────────────────────┘││
 * │└───────────────────────┘│
 * └─────────────────────────┘
 * 上述这种通过一个“基础”组件再叠加各种“附加”功能组件的模式，称之为Filter模式（或者装饰器模式：Decorator）。它可以让我们通过少量的类来实现各种功能的组合：
 *
 *                  ┌─────────────┐
 *                  │ InputStream │
 *                  └─────────────┘
 *                        ▲ ▲
 * ┌────────────────────┐ │ │ ┌─────────────────┐
 * │  FileInputStream   │─┤ └─│FilterInputStream│
 * └────────────────────┘ │   └─────────────────┘
 * ┌────────────────────┐ │     ▲ ┌───────────────────┐
 * │ByteArrayInputStream│─┤     ├─│BufferedInputStream│
 * └────────────────────┘ │     │ └───────────────────┘
 * ┌────────────────────┐ │     │ ┌───────────────────┐
 * │ ServletInputStream │─┘     ├─│  DataInputStream  │
 * └────────────────────┘       │ └───────────────────┘
 *                              │ ┌───────────────────┐
 *                              └─│CheckedInputStream │
 *                                └───────────────────┘
 * 类似的，OutputStream也是以这种模式来提供各种功能：
 *
 *                   ┌─────────────┐
 *                   │OutputStream │
 *                   └─────────────┘
 *                         ▲ ▲
 * ┌─────────────────────┐ │ │ ┌──────────────────┐
 * │  FileOutputStream   │─┤ └─│FilterOutputStream│
 * └─────────────────────┘ │   └──────────────────┘
 * ┌─────────────────────┐ │     ▲ ┌────────────────────┐
 * │ByteArrayOutputStream│─┤     ├─│BufferedOutputStream│
 * └─────────────────────┘ │     │ └────────────────────┘
 * ┌─────────────────────┐ │     │ ┌────────────────────┐
 * │ ServletOutputStream │─┘     ├─│  DataOutputStream  │
 * └─────────────────────┘       │ └────────────────────┘
 *                               │ ┌────────────────────┐
 *                               └─│CheckedOutputStream │
 *                                 └────────────────────┘
 *
 * 注意到在叠加多个FilterInputStream，我们只需要持有最外层的InputStream，
 * <p>
 * 并且，当最外层的InputStream关闭时（在try(resource)块的结束处自动关闭），内层的InputStream的close()方法也会被自动调用，并最终调用到最核心的“基础”InputStream，因此不存在资源泄露
 *
 * @Author jiangyonghua
 * @Date 2020/1/29 12:22
 * @Version 1.0
 **/
public class CountInputStream extends FilterInputStream {

    public static void main(String[] args) throws IOException {
        byte[] msg = "java custom inputstream".getBytes();

        try (CountInputStream cis = new CountInputStream(new ByteArrayInputStream(msg))) {
            while (cis.read() != -1) {

            }
            System.out.println("Total read [" + cis.getByteSend() + "] bytes");
        }
    }

    private int count;

    /**
     * Creates a <code>FilterInputStream</code>
     * by assigning the  argument <code>in</code>
     * to the field <code>this.in</code> so as
     * to remember it for later use.
     *
     * @param in the underlying input stream, or <code>null</code> if
     *           this instance is to be created without an underlying stream.
     */
    protected CountInputStream(InputStream in) {
        super(in);
    }

    public int getByteSend() {
        return this.count;
    }

    @Override
    public int read() throws IOException {
        this.count++;
        return in.read();
    }

}
