package ua.yelisieiev;

import java.io.*;

public class ContentReader {
    private final String contentPath;

    public ContentReader(String contentPath) {
        this.contentPath = contentPath;
    }

    public BufferedInputStream getInputStream(String uri) throws FileNotFoundException {
        File file = new File(contentPath, uri);
        return new BufferedInputStream(new FileInputStream(file));
    }


}
