package net.christophermerrill.update4j.gradle;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FolderSpec
    {
    public String getName()
        {
        return _name;
        }

    public String getBase()
        {
        return _base;
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

    public static FolderSpec parse(String descriptor)
        {
        FolderSpec folder = new FolderSpec();

        StringTokenizer tokenizer = new StringTokenizer(descriptor, "|");
        while (tokenizer.hasMoreTokens())
            {
            StringTokenizer params = new StringTokenizer(tokenizer.nextToken(), "=");
            switch (params.nextToken())
                {
                case "name":
                    folder._name = params.nextToken();
                    break;
                case "base":
                    folder._base = params.nextToken();
                    break;
                case "path":
                    folder._path = params.nextToken();
                    break;
                case "classpath":
                    folder._classpath = true;
                    break;
                case "modulepath":
                    folder._modulepath = true;
                    break;
                case "ignorebootconflict":
                    folder._ignore = true;
                    break;
                }
            }
        return folder;
        }

    private String _name;
    private String _base;
    private String _path;
    private boolean _classpath = false;
    private boolean _modulepath = false;
    private boolean _ignore = false;
    }
