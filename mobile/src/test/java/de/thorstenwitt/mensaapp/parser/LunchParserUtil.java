package de.thorstenwitt.mensaapp.parser;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

/**
 * Created by Herre on 27.02.2017.
 */

public class LunchParserUtil {
    public static InputStream getStubInputStream() throws IOException {
        File file = new File("D:\\AndroidStudioProjects\\mensaApp\\mobile\\src\\test\\res\\input.xml");
        InputStream inputStream = new FileInputStream(file);

        return inputStream;
    }

}
