package com.qc.competition.utils;

import org.apache.commons.io.IOUtils;

import javax.print.attribute.standard.Compression;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Duncan on 27/01/2016.
 */
public class ZipUtils {
    public static final Compression COMPRESSION = Compression.GZIP;
//    private static Logger logger = Logger.getLogger(ZipUtils.class.getName());

    public static byte[] compress(byte[] bytes) {
        byte[] output = null;
        if (bytes == null || bytes.length == 0) {
            output = bytes;
        } else {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream) {{
                    def.setLevel(Deflater.BEST_SPEED);
                }};
                gzipOutputStream.write(bytes);
                gzipOutputStream.close();
                output = byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
//                logger.log(Level.SEVERE, "", e);
                e.printStackTrace();
            }
        }
        return output;
    }

    public static byte[] decompress(byte[] bytes) {
        byte[] output = null;
        if (bytes == null || bytes.length == 0) {
            output = bytes;
        } else {
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
                output = IOUtils.toByteArray(gzipInputStream);
            } catch (IOException e) {
//                logger.log(Level.SEVERE, "", e);
                e.printStackTrace();
            }
        }
        return output;
    }

}
