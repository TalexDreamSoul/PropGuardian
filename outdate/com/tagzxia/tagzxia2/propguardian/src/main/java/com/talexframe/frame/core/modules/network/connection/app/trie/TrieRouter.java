package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.trie;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.trie.TrieNode;
import com.talexframe.frame.core.modules.network.connection.app.subapp.MethodAppReceiver;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app.trie Package }
 *
 * @author TalexDreamSoul 22/07/20 下午 12:11 Project: TalexFrame
 */
public class TrieRouter {

    private final com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.trie.TrieNode root;

    public TrieRouter() {

        root = new com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.trie.TrieNode();
        root.setPath("/");
        root.setSegment("/");

    }

    public TrieRouter addRoute(MethodAppReceiver receiver, String path) {

        if( StringUtils.isEmpty(path) ) return this;

        String strippedPath = StringUtils.strip(path, "/");
        String[] urls = StringUtils.split(strippedPath, "/");

        if ( urls.length != 0 ) {

            com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.trie.TrieNode node = root;
            // for (String url : urls) {
            //
            //     if ( StrUtil.isEmptyIfStr(url) ) continue;
            //
            //     TrieNode child = node.getChild(url);
            //     if ( child == null ) {
            //
            //         child = new TrieNode();
            //         child.setPath(url);
            //         child.setSegment(url);
            //         node.addChild(url, child);
            //
            //     }
            //
            //     node = child;
            //
            // }
            for( String url : urls ) {

               node = addNode(node, receiver, url);

               if( "".equals(url) ) {

                   break;

               }

            }

            node.setPath(path);
            node.setReceiver(receiver);

        }

        return this;

    }

    private com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.trie.TrieNode addNode(com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.trie.TrieNode node, MethodAppReceiver receiver, String segment) {

        //如果是通配符节点，直接返回当前节点：
        if ("**".equals(segment)) {
            node.setWildcard(true);
            return node;
        }

        //如果是动态路由，则创建一个子节点，然后将子节点挂在当前节点下：
        if (segment.startsWith("{") && segment.endsWith("}")) {

            com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.trie.TrieNode childNode = new com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.trie.TrieNode();
            childNode.setSegment(segment);
            node.setDynamicRoute(childNode);
            // node.setReceiver(receiver);

            return childNode;

        }

        com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.trie.TrieNode childNode;

        //静态路由，放到一个Map里，map的key为URL分段，value为新的子节点：
        if (node.getStaticRoutes() == null)
            node.setStaticRoutes(new HashMap<>());
        if (node.getStaticRoutes().containsKey(segment))
            childNode = node.getStaticRoutes().get(segment);
        else {
            childNode = new com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.trie.TrieNode();
            childNode.setSegment(segment);
            node.setDynamicRoute(childNode);
            node.getStaticRoutes().put(segment, childNode);
        }

        return childNode;
    }

    //匹配路由：
    public com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.trie.TrieNode matchRoute(String path) {

        if (!StringUtils.isEmpty(path)) {

            String strippedPath = StringUtils.strip(path, "/");
            String[] strings = StringUtils.split(strippedPath, "/");

            if (strings.length != 0) {

                com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.trie.TrieNode node = root;

                //按照斜杠分割：
                for ( String segment : strings ) {

                    node = matchNode(node, segment);

                    //如果没有匹配到，或者是通配符路由，退出：

                    if ( node == null || node.isWildcard() )
                        break;

                }

                if (node != null)
                    return node;

            }

        }

        return null;
    }

    //匹配子节点：
    private com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.trie.TrieNode matchNode(TrieNode node, String segment) {

        if (node.getStaticRoutes() != null && node.getStaticRoutes().containsKey(segment)) {

            return node.getStaticRoutes().get(segment);

        }

        return node.getDynamicRoute() != null
                ? node.getDynamicRoute() : (node.isWildcard() ? node : null);

    }


}
