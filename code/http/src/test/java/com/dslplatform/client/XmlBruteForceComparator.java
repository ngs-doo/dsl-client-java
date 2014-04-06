package com.dslplatform.client;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A comparator for {@link org.w3c.dom.Element}, compares two XML subtrees. The
 * subtrees are considered equivalent if they contain the same nodes, having the
 * same attributes and values. The order of elements is ignored.
 *
 * For each of the two trees the compiler builds a list of paths root-to-leaf,
 * and compares the two lists.
 *
 * Two nodes are considered equal if their names and values are equal, and they
 * contain the same attributes.
 *
 * Two attributes are equal if their names and values are equal.
 */
public class XmlBruteForceComparator implements Comparator<Element> {

    private final List<List<Node>> xmlPaths_lhs = new ArrayList<List<Node>>();
    private final List<List<Node>> xmlPaths_rhs = new ArrayList<List<Node>>();

    private boolean collapseAllTextContentIntoSingleNode = true;
    private boolean collapseAllCDataContentIntoSingleNode = true;
    private boolean collapseAllCommentsIntoSingleNode = true;

    /**
     * While comparing, collapse all {@link Node.COMMENT_NODE} children of
     * each element into a single {@link Node.COMMENT_NODE}.
     * This will permanently change the tree.
     */
    public void setCollapseAllCommentsIntoSingleNode(
            final boolean collapseAllCommentsIntoSingleNode) {
        this.collapseAllCommentsIntoSingleNode =
                collapseAllCommentsIntoSingleNode;
    }

    /**
     * While comparing, collapse all {@link Node.CDATA_SECTION_NODE} children of
     * each element into a single {@link Node.CDATA_SECTION_NODE}.
     * This will permanently change the tree.
     */
    public void setCollapseAllCDataContentIntoSingleNode(
            final boolean collapseAllCDataContentIntoSingleNode) {
        this.collapseAllCDataContentIntoSingleNode =
                collapseAllCDataContentIntoSingleNode;
    }

    /**
     * While comparing, collapse all {@link Node.TEXT_NODE} children of each
     * element into a single {@link Node.TEXT_NODE}
     * This will permanently change the tree.
     */
    public void setCollapseAllTextContentIntoSingleNode(
            final boolean collapseAllTextContentIntoSingleNode) {
        this.collapseAllTextContentIntoSingleNode =
                collapseAllTextContentIntoSingleNode;
    }

    @Override
    public int compare(final Element lhs, final Element rhs) {

        collapseNodes(lhs, rhs);

        buildPaths(xmlPaths_lhs, null, lhs);
        buildPaths(xmlPaths_rhs, null, rhs);

        return compareAllPaths(xmlPaths_lhs, xmlPaths_rhs);
    }

    private void collapseNodes(final Node lhs, final Node rhs) {
        if (collapseAllTextContentIntoSingleNode) {
            collapseAllContentIntoSingleNode(lhs, Node.TEXT_NODE);
            collapseAllContentIntoSingleNode(rhs, Node.TEXT_NODE);
        }

        if (collapseAllCDataContentIntoSingleNode) {
            collapseAllContentIntoSingleNode(lhs, Node.CDATA_SECTION_NODE);
            collapseAllContentIntoSingleNode(rhs, Node.CDATA_SECTION_NODE);
        }

        if (collapseAllCommentsIntoSingleNode) {
            collapseAllContentIntoSingleNode(lhs, Node.COMMENT_NODE);
            collapseAllContentIntoSingleNode(rhs, Node.COMMENT_NODE);
        }
    }

    private void collapseAllContentIntoSingleNode(
            final Node node,
            final short nodeType) {
        if (node.hasChildNodes()) {
            boolean weFoundAnyTextNodes = false;
            /*
             * Collect all text nodes' text, and remove the nodes from {@link
             * node}
             */
            final StringBuffer collectedTextNodes = new StringBuffer();
            for (final Node child : getListOfChildren(node)) {
                if (child.getNodeType() == nodeType) {
                    collectedTextNodes.append(child.getNodeValue());
                    node.removeChild(child);
                    weFoundAnyTextNodes = true;
                }
            }

            if (weFoundAnyTextNodes) {
                switch (nodeType) {
                    case Node.TEXT_NODE:
                        if (collectedTextNodes.length() > 0) {
                            node.appendChild(node.getOwnerDocument()
                                    .createTextNode(
                                            collectedTextNodes.toString()));
                        }
                        break;
                    case Node.CDATA_SECTION_NODE:
                        node.appendChild(node.getOwnerDocument()
                                .createCDATASection(
                                        collectedTextNodes.toString()));
                        break;
                    case Node.COMMENT_NODE:
                        node.appendChild(node.getOwnerDocument().createComment(
                                collectedTextNodes.toString()));
                        break;
                    default:
                        break;
                }
            }

            /* Rinse and repeat */
            for (final Node child : getListOfChildren(node)) {
                if (child.getNodeType() != nodeType) {
                    collapseAllContentIntoSingleNode(child, nodeType);
                }
            }
        }
    }

    /**
     * Recursively builds a list of all path from root to leaf
     *
     * @param allPaths
     *            The list containing all paths for the current tree
     * @param pathUpToNode
     *            The current path being built; {@code null} in the first step
     * @param node
     *            The current node on the path; root in the first step, a leaf
     *            in the last step
     */
    private void buildPaths(
            final List<List<Node>> allPaths,
            List<Node> pathUpToNode,
            final Node node) {
        if (pathUpToNode == null) {
            pathUpToNode = new ArrayList<Node>();
        }

        pathUpToNode.add(node);

        if (node.hasChildNodes()) {
            for (final Node child : getListOfChildren(node)) {
                buildPaths(allPaths, new ArrayList<Node>(pathUpToNode), child);
            }
        } else {
            allPaths.add(new ArrayList<Node>(pathUpToNode));
            pathUpToNode.clear();
        }
    }

    /**
     * Compares all root-to-leaf paths of the two XML trees.
     *
     * For each path in lhs list find an equivalent path in the rhs list, and
     * remove it if it exists.
     *
     * If for every path in lhs a unique equivalent path in rhs is found, the
     * trees are considered equal.
     *
     * @param lhs
     *            The lhs XML tree paths
     * @param rhs
     *            The rhs XML tree paths
     * @return {@code 0} if they are equal {@code -1} otherwise
     */
    private int compareAllPaths(
            final List<List<Node>> lhs,
            final List<List<Node>> rhs) {
        if (lhs.size() != rhs.size()) {
            System.out.println("Različite veličine:");
            System.out.println();
            System.out.println(lhs);
            System.out.println();
            System.out.println(rhs);

            return -1;
        }

        for (final List<Node> leftPath : lhs) {
            boolean found = false;
            for (final List<Node> rightPath : rhs) {
                if (nodeListsEqual(leftPath, rightPath)) {
                    found = true;
                    rhs.remove(rightPath);

                    break;
                }
            }
            if (!found) {
                out.println(leftPath);
                return -1;
            }
        }

        return 0;
    }

    /**
     * Compares two lists of nodes.
     *
     * The lists are considered equal if their nodes are equal. Since they
     * represent paths in a tree, their ordering is relevant.
     *
     * @param lhs
     *            The lhs List<Node>
     * @param rhs
     *            The rhs List<Node>
     * @return true if the {@code lists} are equal, {@code false} otherwise
     * @throws InterruptedException
     */
    private boolean nodeListsEqual(final List<Node> lhs, final List<Node> rhs) {

        if (lhs.size() != rhs.size()) return false;

        for (int i = 0; i < lhs.size(); i++) {

            final Node node1 = lhs.get(i);
            final Node node2 = rhs.get(i);
            if (!nodesEqual(node1, node2)) return false;
        }

        return true;
    }

    private boolean nodesEqual(final Node node1, final Node node2) {

        if (node1 == null && node2 == null) return true;
        else if (node1 == null || node2 == null) return false;
        else return nodesHaveEqualNames(node1, node2)
                && nodesHaveEqualNumberOfChildren(node1, node2)
                && nodesHaveEqualValues(node1, node2)
                && nodesHaveEqualAttributes(node1, node2);
    }

    private boolean nodesHaveEqualValues(final Node node1, final Node node2) {
        final String node1_value = node1.getNodeValue();
        final String node2_value = node2.getNodeValue();

        return equalsWithNull(node1_value, node2_value);
    }

    private boolean nodesHaveEqualNames(final Node node1, final Node node2) {
        final String node1_name = node1.getNodeName();
        final String node2_name = node2.getNodeName();

        return equalsWithNull(node1_name, node2_name);
    }

    private boolean nodesHaveEqualNumberOfChildren(
            final Node node1,
            final Node node2) {
        return node1.getChildNodes().getLength() == node2.getChildNodes()
                .getLength();
    }

    private boolean nodesHaveEqualAttributes(final Node node1, final Node node2) {
        if (node1.hasAttributes() != node2.hasAttributes()) return false;
        else if (node1.hasAttributes() == false
                && node2.hasAttributes() == false) return true;
        else if (node1.getAttributes().getLength() != node2.getAttributes()
                .getLength()) return false;
        else {
            for (final Attr attr1 : getListOfAttributes(node1)) {
                boolean found = false;
                for (final Attr attr2 : getListOfAttributes(node2)) {
                    if (attributesAreEqual(attr1, attr2)) {
                        found = true;
                        break;
                    }
                }
                if (!found) return false;
            }
        }

        return true;
    }

    private boolean attributesAreEqual(
            final Attr attribute1,
            final Attr attribute2) {
        if (attribute1 == null && attribute2 == null) return true;
        else if (attribute1 == null || attribute2 == null) return false;
        else return equalsWithNull(attribute1.getName(), attribute2.getName())
                && equalsWithNull(attribute1.getValue(), attribute2.getValue());
    }

    private List<Attr> getListOfAttributes(final Node node) {
        final List<Attr> aListOfAttributes = new ArrayList<Attr>();

        for (int i = 0; i < node.getAttributes().getLength(); i++) {
            aListOfAttributes.add((Attr) node.getAttributes().item(i));
        }

        return aListOfAttributes;
    }

    private List<Node> getListOfChildren(final Node node) {
        final List<Node> nodes = new ArrayList<Node>();

        final NodeList nodeList = node.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            nodes.add(nodeList.item(i));
        }

        return nodes;
    }

    private boolean equalsWithNull(final Object o1, final Object o2) {
        if (o1 == null && o2 == null) return true;
        else if (o1 == null || o2 == null) return false;
        else return o1.equals(o2);
    }
}
