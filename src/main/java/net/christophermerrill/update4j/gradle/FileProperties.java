package net.christophermerrill.update4j.gradle;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FileProperties
    {
    public String getPath()
        {
        return _path;
        }

    public void setPath(String path)
        {
System.out.println("The path is now: " + path);
        _path = path;
        }

    private String _path;
    }


