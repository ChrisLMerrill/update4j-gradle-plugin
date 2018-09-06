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

    public static FileSpec parse(String descriptor)
        {
        FileSpec artifact = new FileSpec();

        StringTokenizer tokenizer = new StringTokenizer(descriptor, "|");
        while (tokenizer.hasMoreTokens())
            {
            StringTokenizer params = new StringTokenizer(tokenizer.nextToken(), "=");
            switch (params.nextToken())
                {
                case "name":
                    artifact._name = params.nextToken();
                    break;
                case "path":
                    artifact._path = params.nextToken();
                    break;
                case "classpath":
                    artifact._classpath = true;
                    break;
                case "modulepath":
                    artifact._modulepath = true;
                    break;
                }
            }
        return artifact;
        }

    private String _name;
    private String _path;
    private boolean _classpath = false;
    private boolean _modulepath = false;
    }
