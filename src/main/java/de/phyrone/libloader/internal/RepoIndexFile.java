package de.phyrone.libloader.internal;

import org.msgpack.annotation.Message;

import java.util.HashMap;

@Message
public class RepoIndexFile {
    HashMap<String, LibConfMeta> libs = new HashMap<String, LibConfMeta>();

}

