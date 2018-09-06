package net.christophermerrill.update4j.gradle;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FileSpec
    {
    public String getName()
        {
        return _name;
        }

    public String getPath()
        {
        return _path;
        }

    public boolean isClasspath()
        {
        return _classpath;
        }

    public boolean isModulepath()
        {
        return _modulepath;
        }

    public boolean isIgnoreBootConflict()
        {
        return _ignore;
        }

    public static FileSpec parse(String descriptor)
        {
        FileSpec file = new FileSpec();

        StringTokenizer tokenizer = new StringTokenizer(descriptor, "|");
        while (tokenizer.hasMoreTokens())
            {
            StringTokenizer params = new StringTokenizer(tokenizer.nextToken(), "=");
            switch (params.nextToken())
                {
                case "name":
                    file._name = params.nextToken();
                    break;
                case "path":
                    file._path = params.nextToken();
                    break;
                case "classpath":
                    file._classpath = true;
                    break;
                case "modulepath":
                    file._modulepath = true;
                    break;
                case "ignorebootconflict":
                    file._ignore = true;
                    break;
                }
            }
        return file;
        }

    private String _name;
    private String _path;
    private boolean _classpath = false;
    private boolean _modulepath = false;
    private boolean _ignore = false;
    }
