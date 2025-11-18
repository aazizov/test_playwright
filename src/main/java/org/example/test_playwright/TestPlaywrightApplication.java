package org.example.test_playwright;

import com.microsoft.playwright.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.example.test_playwright.Constants.*;

@SpringBootApplication
@RestController
public class TestPlaywrightApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestPlaywrightApplication.class, args);
    }

    Playwright playwright = InitializePlaywright();

    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name,
                           @RequestParam(value = "mySurname", defaultValue = "World") String surname) {
        return String.format("Hello %s %s!", name, surname );
    }

    @GetMapping("/browser_launch")
    public String browser_launch(@RequestParam(value = "browser", defaultValue = "chromium") String name,
//    public <Browser, BrowserContext, Page> browser_launch(@RequestParam(value = "browser", defaultValue = "chromium") String name,
                           @RequestParam(value = "headless", defaultValue = "false") Boolean value) {
        Browser browser;
        BrowserContext context;
        Page page;

        browser = (Browser) playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setExecutablePath(Paths.get(CHROMIUM_PATH))
                .setHeadless(value)
                .setSlowMo(DELAY_MS));
        context = browser.newContext();
        page = context.newPage();

        return String.format("Browser_launch Browser = %s, Context = %s, Page = %s!",
                browser.toString(), context.toString(), page.toString() );
    }

    static Playwright InitializePlaywright(){
        Playwright playwright;
        Map<String,String> env = new HashMap<>();

        env.put("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD","1"); // For doesn't download Chromium - use Local(already downloaded) version.

        playwright = Playwright.create(new Playwright.CreateOptions().setEnv(env));
//        System.out.println("Playwright initialized.");

        return playwright;
    }


}
