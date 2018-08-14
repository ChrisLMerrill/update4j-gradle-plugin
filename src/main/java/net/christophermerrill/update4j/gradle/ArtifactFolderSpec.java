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

    public void setFolder(String folder)
        {
        _folder = folder;
        }

    public String getBase()
        {
        return _base;
        }

    public void setBase(String base)
        {
        _base = base;
        }

    public static ArtifactFolderSpec parse(String descriptor)
        {
        ArtifactFolderSpec artifact = new ArtifactFolderSpec();

        StringTokenizer tokenizer = new StringTokenizer(descriptor, ":");
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
                }
            }
        return artifact;
        }

    private String _folder;
    private String _base;
    }


