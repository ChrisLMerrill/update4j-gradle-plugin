package net.christophermerrill.update4j.gradle;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ArtifactFolderSpec
    {
    public String getFolder()
        {
        return _folder;
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

    public static ArtifactFolderSpec parse(String descriptor)
        {
        ArtifactFolderSpec artifact = new ArtifactFolderSpec();

        StringTokenizer tokenizer = new StringTokenizer(descriptor, "|");
        while (tokenizer.hasMoreTokens())
            {
            StringTokenizer params = new StringTokenizer(tokenizer.nextToken(), "=");
            switch (params.nextToken())
                {
                case "folder":
                    artifact._folder = params.nextToken();
                    break;
                case "base":
                    artifact._base = params.nextToken();
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

    private String _folder;
    private String _base;
    private String _path;
    private boolean _classpath = false;
    private boolean _modulepath = false;
    }


