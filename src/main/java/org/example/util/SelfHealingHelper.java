package org.example.util;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SelfHealingHelper {
    private final WebDriver driver;
    private final OpenAIClient client;
    private int healedcount = 0;

    public SelfHealingHelper(WebDriver driver, String apiKey) {
        this.driver = driver;
        this.client = OpenAIOkHttpClient.builder()
                .apiKey("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")
                .build();
    }

    public WebElement findElement(By by) {
        try {
            return driver.findElement(by);
        } catch (Exception e) {
            System.out.println("❌ Locator failed: " + by);
            String pageSource = driver.getPageSource();

            ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                    .model("gpt-4o-mini")
                    .addUserMessage("Given this HTML page:\n" + pageSource +
                            "\n\nThe Selenium locator failed: " + by.toString() +
                            "\nSuggest ONLY a valid Selenium locator (XPath or CSS or id or name). No explanation, just the locator.")
                    .maxTokens(100)
                    .temperature(0.2)
                    .build();

            ChatCompletion completion = client.chat().completions().create(params);
            String rawSuggestion = completion.choices().get(0).message()._content().toString();
            System.out.println("✅ AI suggested locator: " + rawSuggestion);

            By healedBy = extractLocator(rawSuggestion);
            healedcount++;

            return driver.findElement((healedBy)); // (simplified)
        }
    }

    private By extractLocator(String text)
    {
        // Normalize
        String cleaned = text.trim()
                .replaceAll("[`]", "")
                .replaceAll("\"", "'")
                .replaceAll("\\r?\\n", " ")
                .trim();

        // 🔥 Handle explicit "By.xxx:" prefixes
        if (cleaned.toLowerCase().startsWith("by.id:")) {
            cleaned = cleaned.replaceFirst("(?i)by\\.id[: ]*", "").trim();
            System.out.println("👉 Using ID: " + cleaned);
            return By.id(cleaned);
        }
        if (cleaned.toLowerCase().startsWith("by.name:")) {
            cleaned = cleaned.replaceFirst("(?i)by\\.name[: ]*", "").trim();
            System.out.println("👉 Using Name: " + cleaned);
            return By.name(cleaned);
        }
        if (cleaned.toLowerCase().startsWith("by.cssselector:")) {
            cleaned = cleaned.replaceFirst("(?i)by\\.cssselector[: ]*", "").trim();
            System.out.println("👉 Using CSS: " + cleaned);
            return By.cssSelector(cleaned);
        }
        if (cleaned.toLowerCase().startsWith("by.xpath:")) {
            cleaned = cleaned.replaceFirst("(?i)by\\.xpath[: ]*", "").trim();
            System.out.println("👉 Using XPath: " + cleaned);
            return By.xpath(cleaned);
        }

        // 🔥 Handle plain "css ..." or "xpath ..."
        if (cleaned.toLowerCase().startsWith("css ")) {
            cleaned = cleaned.substring(4).trim();
            return By.cssSelector(cleaned);
        }
        if (cleaned.toLowerCase().startsWith("xpath ")) {
            cleaned = cleaned.substring(6).trim();
            return By.xpath(cleaned);
        }

        // 🔥 Handle AI returning full By.xxx(...) calls
        if (cleaned.matches("(?i)^by\\.xpath\\s*\\(.*\\)$")) {
            cleaned = cleaned.replaceAll("(?i)^by\\.xpath\\s*\\('?(.*?)'??\\)$", "$1").trim();
            return By.xpath(cleaned);
        }
        if (cleaned.matches("(?i)^by\\.cssselector\\s*\\(.*\\)$")) {
            cleaned = cleaned.replaceAll("(?i)^by\\.cssselector\\s*\\('?(.*?)'??\\)$", "$1").trim();
            return By.cssSelector(cleaned);
        }
        if (cleaned.matches("(?i)^by\\.id\\s*\\(.*\\)$")) {
            cleaned = cleaned.replaceAll("(?i)^by\\.id\\s*\\('?(.*?)'??\\)$", "$1").trim();
            return By.id(cleaned);
        }
        if (cleaned.matches("(?i)^by\\.name\\s*\\(.*\\)$")) {
            cleaned = cleaned.replaceAll("(?i)^by\\.name\\s*\\('?(.*?)'??\\)$", "$1").trim();
            return By.name(cleaned);
        }

        // 🔥 Auto-detect XPath
        if (cleaned.startsWith("/") || cleaned.startsWith("(")) {
            cleaned = cleaned.replaceAll("@([a-zA-Z0-9_-]+)=([^'\"\\]]+)", "@$1='$2'");
            return By.xpath(cleaned);
        }

        // 🔥 Auto-detect CSS
        if (cleaned.startsWith("#") || cleaned.startsWith(".") || cleaned.startsWith("[") || cleaned.contains("[")) {
            return By.cssSelector(cleaned);
        }

        // Simple tag
        if (cleaned.matches("^[a-zA-Z]+$")) {
            return By.tagName(cleaned);
        }

        // ID fallback
        if (cleaned.matches("^[a-zA-Z0-9_-]+$")) {
            return By.id(cleaned);
        }

        // Default fallback: XPath
        return By.xpath(cleaned);

    }

    public int getHealedLocatorCount() {
        return healedcount;
    }


}
