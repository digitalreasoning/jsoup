package org.jsoup.renderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Scanner;

import junit.framework.TestCase;

import org.junit.Test;

public class HtmlToTextRendererTest extends TestCase
{
	@Test
	public void testHtmlToTextRendering() throws FileNotFoundException
	{
		File in = getFile("/htmltests/mime-html-alternative.txt");

		// Can't use APIs from java versions > 1.5 since Jsoup is limited to 1.5 APIs. Using old school methods.
		Scanner scan = new Scanner(in);
		String html = scan.useDelimiter("\\Z").next();
		scan.close();

		HtmlToTextRenderer htmlToTextRenderer = new HtmlToTextRenderer(new RendererConfig.RendererConfigBuilder().createRendererConfig());
		System.out.println(htmlToTextRenderer.render(html));

	}

	public static File getFile(String resourceName)
	{
		try
		{
			File file = new File(HtmlToTextRendererTest.class.getResource(resourceName).toURI());
			return file;
		}
		catch (URISyntaxException e)
		{
			throw new IllegalStateException(e);
		}
	}
}