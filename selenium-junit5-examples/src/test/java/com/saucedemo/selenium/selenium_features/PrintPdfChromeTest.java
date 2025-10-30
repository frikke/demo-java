package com.saucedemo.selenium.selenium_features;

import com.saucedemo.selenium.TestBase;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.PrintsPage;
import org.openqa.selenium.print.PrintOptions;

public class PrintPdfChromeTest extends TestBase {
  public static Path directory;

  @BeforeEach
  public void setup(TestInfo testInfo) throws IOException {
    // PDF printing is only working in headless old mode
    startChromeSession(testInfo, List.of("--headless"));
    driver.navigate().to("https://www.saucedemo.com/");
    Cookie cookie =
        new Cookie.Builder("session-username", "session-username")
            .domain("www.saucedemo.com")
            .path("/")
            .build();
    driver.manage().addCookie(cookie);
    driver.navigate().to("https://www.saucedemo.com/inventory.html");
    directory = Files.createTempDirectory("chrome-");
    directory.toFile().deleteOnExit();
  }

  @Test
  public void printPage() throws IOException {
    Pdf print = ((PrintsPage) driver).print(new PrintOptions());
    Path printPage = Paths.get(directory + "PrintPage.pdf");
    Files.write(printPage, OutputType.BYTES.convertFromBase64Png(print.getContent()));
    Assertions.assertTrue(printPage.toFile().exists());
  }
}
