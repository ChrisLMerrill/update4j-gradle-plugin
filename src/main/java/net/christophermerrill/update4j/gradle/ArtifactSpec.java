package net.christophermerrill.update4j.gradle;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ArtifactSpec
    {
    public String getFile()
        {
        return _file;
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

    public static ArtifactSpec parse(String descriptor)
        {
        ArtifactSpec artifact = new ArtifactSpec();

        StringTokenizer tokenizer = new StringTokenizer(descriptor, "|");
        while (tokenizer.hasMoreTokens())
            {
            StringTokenizer params = new StringTokenizer(tokenizer.nextToken(), "=");
            switch (params.nextToken())
                {
                case "file":
                    artifact._file = params.nextToken();
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

    private String _file;
    private String _path;
    private boolean _classpath = false;
    private boolean _modulepath = false;
    }
