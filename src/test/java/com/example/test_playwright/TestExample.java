package com.example.test_playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.*;

import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.Timing;
import org.testng.annotations.*;

import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.example.test_playwright.Constants.*;
import static org.testng.Assert.*;


public class TestExample {
    // Shared between all tests in this class.
    static Playwright playwright;
    static Browser browser;

    // New instance for each test method.
    BrowserContext context;
    Page page;

    static Map<String,String> env = new HashMap<>();


    @BeforeClass
    void launchBrowser() {
//        System.out.println("@BeforeClass");
        env.put("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD","1"); // For doesn't download Chromium - use Local(already downloaded) version.
        playwright = Playwright.create(new Playwright.CreateOptions().setEnv(env));
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setExecutablePath(Paths.get(CHROMIUM_PATH))
                .setHeadless(INVISIBILITY)
                .setSlowMo(DELAY_MS));
    }

    @AfterClass
    void closeBrowser() {
        playwright.close();
    }

/*
    @BeforeSuite
    void startBrowser() {
        System.out.println("@BeforeSuite");
    }
*/

    @BeforeMethod
    void createContextAndPage() throws InterruptedException {
//        System.out.println("@BeforeMethod");
        context = browser.newContext();
        page = context.newPage();

        page.onRequestFinished(request -> {
            Timing timing = request.timing();
//            System.out.println(timing.responseEnd - timing.startTime);
            System.out.println(LocalDateTime.ofInstant(Instant.ofEpochMilli((long) (timing.startTime)),
                    ZoneOffset.ofHours(4)) + " : " + timing.responseEnd + " ms.\t" + request.url());
        });

// @Enter to Tekila
        page.navigate(LOGIN_PATH);
// Enter Login Page
        Locator login = page.locator("#usr");
        login.fill(LOGIN);
        Locator password = page.locator("#pass");
        password.fill(PASSWORD);
        Locator buttonLogin = page.locator("#login-button");
        buttonLogin.click();
//        Thread.sleep(10000);
//        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("Tekila UI.png")));
    }

    @AfterMethod
    void closeContext() {
        context.close();
    }

/*
    @Test
    void shouldClickButton() {
        page.navigate("data:text/html,<script>var result;</script><button onclick='result=\"Clicked\"'>Go</button>");
        page.locator("button").click();
        assertEquals(page.evaluate("result"), "Clicked");
    }

    @Test
    void shouldCheckTheBox() {
        page.setContent("<input id='checkbox' type='checkbox'></input>");
        page.locator("input").check();
        assertTrue((Boolean) page.evaluate("() => window['checkbox'].checked"));
    }

    @Test
    void shouldSearchWiki() {
        page.navigate("https://www.wikipedia.org/");
        page.locator("input[name=\"search\"]").click();
        page.locator("input[name=\"search\"]").fill("playwright");
        page.locator("input[name=\"search\"]").press("Enter");
        assertEquals(page.url(), "https://en.wikipedia.org/wiki/Playwright");
    }
*/
    @Test
    void shouldOpenBBTV() throws InterruptedException {
//        Thread.sleep(DELAY_MS);
// Click on the "New Billing UI Test env" Button
        Locator newBillingUi = page.getByText("Billing UI 138");
        newBillingUi.click();
// Click on the "Add" Dropdown(Listbox)
        Locator spanAdd = page.getByText("Add");
        spanAdd.click();
// Click on the "Individual" Element from Listbox
        Locator spanIndividual = page.getByText("Individual");
        spanIndividual.click();
// Click on the "Choose a Provider" Button
        Locator spanChooseProvider = page.getByText("Choose a Provider");
        spanChooseProvider.click();
// Check that BBTV provider exists, otherwise get Screenshot


        Integer bbtvProvider = page.getByText(PROVIDER_NAME).count();
        List<String> providersList = page.getByText("").allInnerTexts();
        if(bbtvProvider == 0){
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("Provider " + PROVIDER_NAME +" Not Found.png")));
//          make changes in Excel and Exit
        } else if (bbtvProvider > 1) {
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("Providers " + PROVIDER_NAME + " are Too Many.png")));
//          make changes in Excel and Exit
        }

        System.out.println(PROVIDER_NAME +  " found = " + page.getByText(PROVIDER_NAME).count());

    }


}