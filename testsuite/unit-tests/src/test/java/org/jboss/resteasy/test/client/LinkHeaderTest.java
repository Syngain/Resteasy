package org.jboss.resteasy.test.client;

import javax.ws.rs.core.Link;

import org.jboss.resteasy.plugins.delegates.LinkHeaderDelegate;
import org.jboss.resteasy.specimpl.LinkBuilderImpl;
import org.jboss.resteasy.spi.LinkHeader;
import org.junit.Assert;
import org.junit.Test;

/**
 * @tpSubChapter Resteasy-client
 * @tpChapter Client tests
 * @tpTestCaseDetails Test for LinkHeader (it use deprecated Link). Test also new Link provided by jax-rs 2.0 spec
 * @tpSince RESTEasy 3.0.16
 */
public class LinkHeaderTest {

    /**
     * @tpTestDetails Test for deprecated Link, LinkHeader and LinkHeaderDelegate. Basic test.
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testTopic() throws Exception {
        LinkHeaderDelegate delegate = new LinkHeaderDelegate();
        LinkHeader header = delegate.fromString("<http://localhost:8081/linkheader/topic/sender>; rel=\"sender\"; title=\"sender\", <http://localhost:8081/linkheader/topic/poller>; rel=\"top-message\"; title=\"top-message\"");
        Link sender = header.getLinkByTitle("sender");
        Assert.assertNotNull("Link should not be null", sender);
        Assert.assertEquals("Wrong url", "http://localhost:8081/linkheader/topic/sender", sender.getUri().toString());
        Assert.assertEquals("Wrong rel", "sender", sender.getRel());
        Link top = header.getLinkByTitle("top-message");
        Assert.assertNotNull(top);
        Assert.assertEquals("Wrong URL in link", "http://localhost:8081/linkheader/topic/poller", top.getUri().toString());
        Assert.assertEquals("Wrong rel in link", "top-message", top.getRel());

    }

    /**
     * @tpTestDetails Test for deprecated Link, LinkHeader and LinkHeaderDelegate. More complex URLs are used.
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testTopicComplex() throws Exception {
        LinkHeaderDelegate delegate = new LinkHeaderDelegate();
        LinkHeader header = delegate.fromString("<http://localhost:8081/topics/test/poller/next?index=0>; rel=\"next-message\"; title=\"next-message\",<http://localhost:8081/topics/test/poller>; rel=\"generator\"; title=\"generator\"");
        Link next = header.getLinkByTitle("next-message");
        Assert.assertNotNull("Link is not found", next);
        Assert.assertEquals("Wrong URL in link", "http://localhost:8081/topics/test/poller/next?index=0", next.getUri().toString());
        Assert.assertEquals("Wrong rel in link", "next-message", next.getRel());
        Link generator = header.getLinkByTitle("generator");
        Assert.assertNotNull(generator);
        Assert.assertEquals("Wrong URL in link", "http://localhost:8081/topics/test/poller", generator.getUri().toString());
        Assert.assertEquals("Wrong rel in link", "generator", generator.getRel());
    }

    /**
     * @tpTestDetails Test add method in LinkHeader class.
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testAdd() {
        final LinkHeader linkHeader = new LinkHeader();
        Assert.assertEquals("Wrong size of linkHeader", linkHeader.getLinks().size(), 0);
        linkHeader.addLink(new LinkBuilderImpl().title("one").rel("resl-1").uri("href-1").build());
        Assert.assertEquals("Wrong size of linkHeader", linkHeader.getLinks().size(), 1);
        linkHeader.addLink(new LinkBuilderImpl().title("two").rel("resl-2").uri("href-2").build());
        Assert.assertEquals("Wrong size of linkHeader", linkHeader.getLinks().size(), 2);
    }

    /**
     * @tpTestDetails Test new client without API and old client with API
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testLinkheader() throws Exception {
        LinkHeaderDelegate delegate = new LinkHeaderDelegate();
        LinkHeader header = delegate.fromString("<http://example.com/TheBook/chapter2>; rel=\"previous\";\n" +
                "         title=\"previous chapter\"");

        Assert.assertTrue("Link is not present", header.getLinksByTitle().containsKey("previous chapter"));
        Assert.assertTrue("Wrong link", header.getLinksByRelationship().containsKey("previous"));
        Assert.assertEquals("Wrong link", header.getLinksByTitle().get("previous chapter").getUri().toString(), "http://example.com/TheBook/chapter2");
        String str = delegate.toString(header);
        header = delegate.fromString(str);
        Assert.assertTrue("Wrong link", header.getLinksByTitle().containsKey("previous chapter"));
        Assert.assertTrue("Wrong link", header.getLinksByRelationship().containsKey("previous"));
        Assert.assertEquals("Wrong link", header.getLinksByTitle().get("previous chapter").getUri().toString(), "http://example.com/TheBook/chapter2");
    }
}
