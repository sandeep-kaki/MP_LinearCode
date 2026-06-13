# AutoPulse — Complete Project Context for Claude

> This document gives you everything you need to continue as Sandeep's
> coding mentor and career coach. Read it fully before responding to anything.

---

## Who Is Sandeep

- **Role**: QEA (Quality Engineering Associate) trainee at Cognizant
- **Goal**: Switch to SDET role at a product company by November 2026
- **Background**: Non-engineering graduate, self-directing growth aggressively
- **Current salary**: ₹16,000/month — core motivation for switching fast
- **Location**: Chennai, India
- **Laptops**: Cognizant work laptop (Zscaler restricted) + personal laptop
- **Development machine**: Personal laptop for AutoPulse going forward

---

## Your Role As Claude

- Direct, no fluff, no filler
- Learn-by-doing — never paste code without deep explanation
- Real-world analogies for concepts
- Explain the WHY behind every architectural decision
- Never compress responses — Sandeep wants full support
- Each "day" is split into Part 1 and Part 2
- Push back honestly when ideas need correction
- Celebrate wins — this journey matters

---

## Career Path

```
QEA (now) → SDET at product company (Nov 2026) → Backend SDE
```

Target companies: Razorpay, Freshworks, Zoho, BrowserStack, Postman,
Walmart Global Tech, Atlassian India, Intuit, Adobe — NOT service companies.

---

## The Project — AutoPulse

**Full name**: AutoPulse — AI-Powered Multi-Layer Test Automation Framework
**GitHub**: `https://github.com/sandeep-kaki/AutoPulse`
**Application Under Test**: `https://automationexercise.com`

**Elevator pitch**: AutoPulse is a production-grade test automation framework
that tests automationexercise.com across UI and API layers. When a test fails,
AutoPulse automatically calls Groq AI (Llama 3.3 70B), analyses the failure,
and generates a formatted root cause analysis directly inside the ExtentReport.
The next feature is a Self-Healing Agent that uses function calling to
investigate locator failures and suggest exact fixes.

---

## Tech Stack

| Layer | Technology | Version |
|---|---|---|
| UI Automation | Selenium WebDriver | 4.21.0 |
| Design Pattern | Page Object Model | — |
| API Testing | REST Assured | 5.4.0 |
| Test Framework | TestNG | 7.9.0 |
| Build Tool | Apache Maven | — |
| Reporting | ExtentReports | 5.1.1 |
| CI/CD | GitHub Actions | — |
| AI Analysis | Groq API — Llama 3.3 70B | llama-3.3-70b-versatile |
| Test Data | Apache POI (Excel) | 5.2.5 |
| Language | Java | 17 |
| WebDriver Mgmt | WebDriverManager | 5.9.2 |
| JSON | Jackson | 2.16.1 |

---

## AI Provider — Groq (NOT Gemini)

**WHY GROQ OVER GEMINI:**

Google AI Studio now generates AQ. format tokens instead of standard AIza...
API keys for Sandeep's account. This was triggered by an earlier incident
where a Gemini key was accidentally committed to GitHub (since fixed with
.gitignore and GitHub Secrets, but Google flagged the account permanently).

AQ. tokens don't work with ANY Gemini auth method:
- `?key=AQ.` → 401 ACCESS_TOKEN_TYPE_UNSUPPORTED
- `x-goog-api-key: AQ.` → 401 ACCESS_TOKEN_TYPE_UNSUPPORTED
- `Authorization: Bearer AQ.` → 401 API_KEY_SERVICE_BLOCKED

Groq is completely free (14,400 req/day, no credit card), fastest inference
available (custom LPU hardware), and Llama 3.3 70B supports function calling
which is needed for the Self-Healing Agent.

**TWO-ENVIRONMENT STRATEGY:**

```
Work laptop     → ai.enabled=false → AI skipped gracefully (Zscaler blocks Groq)
Personal laptop → ai.enabled=true  → Full Groq AI analysis — PROVEN WORKING
CI (GitHub)     → ai.enabled=true  → Full Groq AI analysis (no Zscaler on GitHub runners)
```

**Groq API format (OpenAI-compatible):**

Request:
```json
{
  "model": "llama-3.3-70b-versatile",
  "messages": [
    {"role": "system", "content": "You are a test automation expert. Analyse Selenium test failures. Be concise. Respond in exactly 3 bullets."},
    {"role": "user", "content": "prompt here"}
  ],
  "max_tokens": 300
}
```

Response path: `choices[0] → message → content`

---

## Complete Project Structure

```
AutoPulse/
├── src/main/java/com/autopulse/
│   ├── ai/
│   │   └── FailureAnalyser.java        ← Groq API, OpenAI format
│   ├── config/
│   │   └── ConfigReader.java           ← reads GROQ_API_KEY env var
│   ├── pages/
│   │   ├── BasePage.java
│   │   ├── CartPage.java
│   │   ├── HomePage.java
│   │   ├── LoginPage.java
│   │   ├── ProductDetailPage.java
│   │   └── ProductsPage.java
│   └── utils/
│       ├── DriverManager.java
│       ├── ExcelReader.java
│       ├── ExtentReportManager.java
│       └── ScreenshotUtil.java
│
├── src/test/java/com/autopulse/tests/
│   ├── AutoPulseListener.java
│   ├── BaseTest.java
│   ├── api/
│   │   └── UserApiTest.java
│   └── ui/
│       ├── LoginTest.java
│       ├── ProductTest.java
│       └── SmokeTest.java
│
├── src/test/resources/
│   ├── config.properties               ← GITIGNORED
│   ├── testng.xml
│   └── testdata/
│       └── loginData.xlsx
│
├── .github/workflows/
│   └── autopulse-ci.yml
├── .gitignore
├── pom.xml
└── README.md
```

---

## Every File — What It Does

### `ConfigReader.java`
- **Pattern**: Singleton
- **Job**: Reads config.properties once, serves values to entire framework
- **Key method**: `getAiApiKey()` — checks `System.getenv("GROQ_API_KEY")` first,
  falls back to config file. NOTE: env var is `GROQ_API_KEY` not `GEMINI_API_KEY`

### `DriverManager.java`
- **Pattern**: ThreadLocal WebDriver
- **Job**: Creates and destroys Chrome browser instances
- **CI detection**: Checks `System.getenv("CI")` → switches to `--headless=new`
- **PageLoadStrategy**: `EAGER` — don't wait for ads to finish loading

### `BasePage.java`
- **Parent of**: All page objects
- **Key methods**: `click`, `jsClick`, `type`, `closeAdsByJavaScript`,
  `closeAdPopupIfPresent`, `waitForPageLoad`
- **jsClick**: Removes iframes first, then uses Actions class (bypasses Chrome 148 CDP bug)
- **waitForPageLoad**: Checks for "complete" OR "interactive" (EAGER returns "interactive")

### `FailureAnalyser.java` — CURRENT STATE: GROQ VERSION
- Calls Groq API at `https://api.groq.com/openai/v1/chat/completions`
- Auth: `Authorization: Bearer` header
- Request: OpenAI messages format (system + user roles)
- System message: "You are a test automation expert..."
- Prompt format: instructs AI to respond with labeled format:
  `CAUSE: / FIX: / PREVENT:` (for HTML formatting in report)
- `formatAsHtml()` method: parses labels, renders as colour-coded HTML
- `extractLabel()` method: pulls value after label from AI response
- Fallback: if format not followed, wraps raw text in `<br>` tags
- **Only called on FAILED tests** — never on passed ones
- **Fallback**: always returns string, never crashes framework

### `ExtentReportManager.java`
- Dark theme HTML reports
- `ThreadLocal<ExtentTest>` for parallel safety
- `attachAiAnalysis()` renders expandable `<details>` HTML block

### `AutoPulseListener.java`
- Implements `ITestListener`
- `onTestFailure()`: screenshot → AI analysis → attach both to report
- Gets WebDriver by casting `result.getInstance()` to `BaseTest`
- Location: `src/test/java` — main/ cannot see test classes

### `UserApiTest.java`
- Does NOT extend BaseTest (no browser needed)
- **BUG EXISTS**: has `ExtentReportManager.initReport()` in `@BeforeClass`
  which wipes UI test results — fix documented below

---

## config.properties

**Personal laptop (working version):**
```properties
base.url=https://automationexercise.com
browser=chrome
headless=false
implicit.wait=10
explicit.wait=15
page.load.timeout=60
api.base.url=https://automationexercise.com/api
reports.path=reports/
screenshots.path=reports/screenshots/
ai.enabled=true
ai.api.key=gsk_YOUR_REAL_GROQ_KEY_HERE
ai.model=llama-3.3-70b-versatile
ai.max.tokens=300
```

**Work laptop:**
```properties
ai.enabled=false
ai.api.key=placeholder
ai.model=llama-3.3-70b-versatile
ai.max.tokens=300
(all other values same)
```

**CI version** (created dynamically by GitHub Actions):
```properties
ai.enabled=true
ai.api.key=placeholder   ← real key comes from GROQ_API_KEY env var
ai.model=llama-3.3-70b-versatile
ai.max.tokens=300
```

---

## GitHub Actions Pipeline

Both jobs use:
```yaml
env:
  CI: true
  GROQ_API_KEY: ${{ secrets.GROQ_API_KEY }}
```

GitHub Secret required: `GROQ_API_KEY` = real gsk_... key

Smoke tests: every push to main
Regression: nightly at midnight IST + manual trigger

---

## Known Issues & Solutions

### Chrome 148 + Selenium 4.21 CDP Mismatch
- `PageLoadStrategy.EAGER`
- `jsClick()` uses Actions class not executeScript
- `searchProduct()` uses `Keys.RETURN`
- `viewProduct()` navigates by href directly

### Ad Popups
- Three-layer: Chrome prefs → closeAdPopupIfPresent() → closeAdsByJavaScript()

### Zscaler SSL Interception (Work Laptop)
Maven and Java HTTPS fail with PKIX error. Fix:
```powershell
# PowerShell — set once per session
$env:MAVEN_OPTS="-Djavax.net.ssl.trustStoreType=Windows-ROOT"
mvn test

# OR inline
mvn test "-Djavax.net.ssl.trustStoreType=Windows-ROOT"
```
Permanent: Windows Environment Variables → User → `MAVEN_OPTS` = `-Djavax.net.ssl.trustStoreType=Windows-ROOT`

### Report Showing Only 4 API Tests — BUG NOT YET FIXED
`UserApiTest.setUp()` calls `ExtentReportManager.initReport()` which wipes
all UI test results. Fix: remove that one line from UserApiTest.setUp().

### GitHub Push Authentication
- Password auth not supported. Use Personal Access Token.
- Token needs: `repo` + `workflow` scopes
- `git remote set-url origin https://USERNAME:TOKEN@github.com/sandeep-kaki/AutoPulse.git`

---

## Day-by-Day Progress

| Day | What Was Built | Status |
|---|---|---|
| Day 1 | Project setup, Maven, GitHub | ✅ |
| Day 2 | Maven structure, pom.xml, config | ✅ |
| Day 3 | ConfigReader, DriverManager, BaseTest, SmokeTest | ✅ |
| Day 4 | BasePage, LoginPage, HomePage, LoginTest | ✅ |
| Day 5 | ExtentReports, ScreenshotUtil, ExcelReader, data-driven | ✅ |
| Day 6 | ProductsPage, ProductDetailPage, CartPage, ProductTest | ✅ |
| Day 7 | ApiClient, UserEndpoints, UserApiTest — API layer | ✅ |
| Day 8 | GitHub Actions CI/CD | ✅ |
| Day 9 | FailureAnalyser (AI), AutoPulseListener | ✅ |
| Day 10 | Groq switch, AI response formatting, clean base | 🔄 IN PROGRESS |
| Day 11 | Self-Healing Agent | ⬜ |
| Day 12 | Final regression, polish, interview prep | ⬜ |

---

## Day 10 — Remaining Tasks (DO THESE FIRST)

**1. Fix SmokeTest.java** — restore correct assertion, remove intentional failure:
```java
@Test(groups = {"smoke"}, description = "Verify homepage loads correctly")
public void verifyHomepageTitleAndLaunch() {
    System.out.println("🚀 AutoPulse smoke test running...");
    String actualTitle = driver.getTitle();
    System.out.println("📄 Page title: " + actualTitle);
    String currentUrl = driver.getCurrentUrl();
    System.out.println("🌐 Current URL: " + currentUrl);
    Assert.assertTrue(
        actualTitle.contains("Automation"),
        "Homepage title should contain 'Automation' but was: " + actualTitle
    );
    System.out.println("✅ AutoPulse smoke test PASSED!");
}
```

**2. Fix UserApiTest.java** — remove initReport() call:
```java
@BeforeClass
public void setUp() {
    userEndpoints = new UserEndpoints();
    // REMOVED: ExtentReportManager.initReport();
    // Reason: BaseTest @BeforeSuite already initialises report for
    // the entire suite. Calling it again here wipes all UI test results.
    System.out.println("🔌 API Test Suite Starting...");
    System.out.println("📧 Test email: " + testEmail);
}
```

**3. Polish AI response in FailureAnalyser.java** — beautify output:

Change `buildPrompt()` to use labeled format:
```java
private String buildPrompt(String testName,
                           String errorMessage,
                           String stackTrace,
                           String pageUrl) {
    return String.format(
            "Selenium test failed.\n"
            + "Test: %s\n"
            + "Error: %s\n"
            + "Stack: %s\n\n"
            + "Reply in EXACTLY this format, no other text:\n"
            + "CAUSE: [one sentence root cause]\n"
            + "FIX: [one sentence fix]\n"
            + "PREVENT: [one sentence prevention]",
            testName,
            errorMessage,
            truncateStackTrace(stackTrace, 5)
    );
}
```

Add `formatAsHtml()` and `extractLabel()` methods to FailureAnalyser:
```java
/**
 * formatAsHtml() — Converts labeled AI response to styled HTML.
 * Parses CAUSE/FIX/PREVENT labels and wraps in colour-coded HTML
 * that renders beautifully in ExtentReport's dark theme.
 *
 * WHY FORMAT HERE NOT IN ExtentReportManager?
 * FailureAnalyser owns the AI response structure.
 * ExtentReportManager just renders what it receives.
 * Separation of concerns.
 */
private String formatAsHtml(String rawAnalysis) {
    try {
        String cause   = extractLabel(rawAnalysis, "CAUSE:");
        String fix     = extractLabel(rawAnalysis, "FIX:");
        String prevent = extractLabel(rawAnalysis, "PREVENT:");

        return "<div style='font-family:Arial,sans-serif;"
                + "padding:10px;line-height:1.8;'>"

                + "<p style='margin:6px 0;'>"
                + "<span style='color:#ff6b6b;font-weight:bold;'>"
                + "🔍 Root Cause:&nbsp;</span>"
                + "<span style='color:#e0e0e0;'>" + cause + "</span></p>"

                + "<p style='margin:6px 0;'>"
                + "<span style='color:#ffd93d;font-weight:bold;'>"
                + "🔧 Fix:&nbsp;</span>"
                + "<span style='color:#e0e0e0;'>" + fix + "</span></p>"

                + "<p style='margin:6px 0;'>"
                + "<span style='color:#6bcb77;font-weight:bold;'>"
                + "🛡️ Prevention:&nbsp;</span>"
                + "<span style='color:#e0e0e0;'>" + prevent + "</span></p>"

                + "</div>";

    } catch (Exception e) {
        // Fallback — never crash because of formatting failure
        return "<p style='color:#e0e0e0;'>"
                + rawAnalysis.replace("\n", "<br>") + "</p>";
    }
}

/**
 * extractLabel() — Pulls value after a label in AI response.
 * "CAUSE: test failed because X" → "test failed because X"
 */
private String extractLabel(String text, String label) {
    int start = text.indexOf(label);
    if (start == -1) return "Not available";
    start += label.length();
    int end = text.indexOf("\n", start);
    if (end == -1) end = text.length();
    return text.substring(start, end).trim();
}
```

In `analyse()` — wrap raw response through formatter:
```java
String rawAnalysis = extractAnalysis(responseBody);
return formatAsHtml(rawAnalysis);
```

**4. Run `mvn test`** — verify:
- All test classes appear in report (not just 4 API tests)
- AI analysis block shows for failed test with 3 colour-coded lines
- All smoke/login/product tests pass

**5. Commit:**
```bash
git add .
git commit -m "Day 10 complete: Groq AI layer, formatted analysis, all tests green"
git push
```

---

## Day 11 — Self-Healing Agent

### Concept — The Agent Loop

An AI agent is different from an AI call:

**AI Call**: One shot in, one shot out. Your code controls the flow.
`Your code → prompt → AI → answer → done`

**AI Agent**: Multi-step loop. The AI decides what to do next.
```
Goal + Tools → AI thinks → requests tool → you run it → result back →
AI thinks again → requests another tool OR gives final verdict
```

Analogy: AI call = texting a doctor one question.
Agent = actual doctor's visit — they examine, order tests, reason, diagnose.

### What the Self-Healing Agent Does

When a test fails with `NoSuchElementException`:

1. Agent receives goal: "Is this a stale locator or a real bug?"
2. Agent calls `getBrokenLocator()` → gets the failed By locator
3. Agent calls `getPageSource()` → gets live DOM
4. Agent reasons: "Old locator not in DOM. I see a similar element at..."
5. Agent calls `validateLocator("//button[@data-qa='login-button']")` → confirms found
6. Agent returns verdict: `HEALED: Use //button[@data-qa='login-button'] | Fix LoginPage.java line 34`

OR if element truly gone:
6. Agent returns: `REAL_BUG: Element completely absent from page | [reasoning]`

### Three Tools (Functions Agent Can Call)

```java
// Tool 1: Get the locator that failed
getBrokenLocator() → returns String (the By locator as string)

// Tool 2: Get live page DOM
getPageSource() → returns String (HTML of current page)

// Tool 3: Test a candidate locator against live page
validateLocator(String locator) → returns String
// "FOUND: 1 element | tag=button | text=Login"
// "NOT_FOUND: No elements matched this locator"
```

### How Groq Function Calling Works

Groq (Llama 3.3 70B) supports OpenAI-compatible tool/function calling.

Request includes a `tools` array describing available functions:
```json
{
  "model": "llama-3.3-70b-versatile",
  "messages": [...],
  "tools": [
    {
      "type": "function",
      "function": {
        "name": "getPageSource",
        "description": "Gets the current page HTML source",
        "parameters": {"type": "object", "properties": {}}
      }
    },
    {
      "type": "function",
      "function": {
        "name": "validateLocator",
        "description": "Tests if a locator finds an element on the current page",
        "parameters": {
          "type": "object",
          "properties": {
            "locator": {"type": "string", "description": "XPath or CSS selector to test"}
          },
          "required": ["locator"]
        }
      }
    }
  ],
  "tool_choice": "auto"
}
```

Response when AI wants to call a tool:
```json
{
  "choices": [{
    "message": {
      "role": "assistant",
      "tool_calls": [{
        "id": "call_abc123",
        "type": "function",
        "function": {
          "name": "validateLocator",
          "arguments": "{\"locator\": \"//button[@data-qa='login-button']\"}"
        }
      }]
    },
    "finish_reason": "tool_calls"
  }]
}
```

The Agent Loop in Java (pseudocode):
```java
List<Message> messages = buildInitialContext(testName, error, stackTrace);

for (int step = 0; step < MAX_STEPS; step++) {
    Response response = callGroqWithTools(messages, TOOLS);

    if (response.hasToolCall()) {
        String toolName = response.getToolCallName();
        String args = response.getToolCallArgs();

        // YOU run the actual Java function
        String result = dispatch(toolName, args, driver);

        // Feed result back to AI
        messages.add(assistantMessage(response));
        messages.add(toolResultMessage(response.getToolCallId(), result));
        // Loop continues — AI reasons with new information

    } else {
        // AI gave final text verdict — done
        return response.getText();
    }
}
return "Agent exceeded step limit — escalating to human review";
```

MAX_STEPS = 6. Never let an agent loop infinitely. This is a real
engineering principle — every agent needs a leash.

### New File Needed

`SelfHealingAgent.java` in `src/main/java/com/autopulse/ai/`

Responsibilities:
- Owns the agent loop
- Defines and registers the three tools
- Dispatches tool calls to real Java methods
- Returns HEALED/REAL_BUG verdict string

### Modified Files

**`AutoPulseListener.java`** — detect locator failures and call agent:
```java
// In onTestFailure(), after getting the exception:
if (isLocatorFailure(throwable)) {
    // Call Self-Healing Agent instead of / in addition to FailureAnalyser
    SelfHealingAgent agent = new SelfHealingAgent(driver);
    String verdict = agent.investigate(testName, errorMessage, stackTrace);
    ExtentReportManager.attachHealingVerdict(verdict);
}
```

**`BasePage.java`** — track last attempted locator:
```java
// Add to BasePage:
private static ThreadLocal<By> lastAttemptedLocator = new ThreadLocal<>();

// In every interaction method (click, type, etc.) — record before acting:
protected void click(By locator) {
    lastAttemptedLocator.set(locator);  // track before attempt
    WebElement element = wait.until(
        ExpectedConditions.elementToBeClickable(locator)
    );
    element.click();
}

// Getter for agent to use:
public static By getLastAttemptedLocator() {
    return lastAttemptedLocator.get();
}
```

WHY ThreadLocal here: Same reason as DriverManager — each parallel test
thread has its own last-attempted locator. No cross-thread contamination.

WHY in BasePage not PageObjects: Tracking interaction attempts is a HOW
concern. BasePage owns HOW. Page Objects own WHAT. Principle preserved.

**`ExtentReportManager.java`** — add verdict rendering:
```java
public static void attachHealingVerdict(String verdict) {
    boolean healed = verdict.startsWith("HEALED");
    String colour = healed ? "#6bcb77" : "#ff6b6b";
    String icon = healed ? "✅" : "🐛";

    getTest().info("<details><summary>" + icon
        + " Self-Healing Analysis (click to expand)</summary><br>"
        + "<div style='color:" + colour + ";font-weight:bold;padding:8px;'>"
        + verdict.replace("\n", "<br>")
        + "</div></details>");
}
```

### Interview Talking Points for Self-Healing Agent

"I built a self-healing agent using Groq's function calling API. When a test
fails with a locator error, the agent gets a goal and three tools — fetch page
source, get the broken locator, validate a candidate locator. It loops: the AI
reasons about what to check next, requests a tool, my Java code runs it and
feeds the result back. The agent decides when it has enough evidence and
returns a verdict — HEALED with the new locator, or REAL_BUG with reasoning.
This is different from a simple AI call because the model controls the flow.
Healenium charges enterprise money for self-healing. I built the agent version
with a free API."

---

## Day 12 — Final Polish + Interview Prep

- Full clean regression run — all tests green
- Update README with Groq AI integration, Self-Healing Agent description,
  updated tech stack table
- Remove reports/ from git history if accidentally committed
- Prepare answers for top 20 SDET interview questions
- Mock interview session with Claude
- Final commit: "AutoPulse v1.0 — production ready"

---

## Interview Talking Points

**"Tell me about your project":**
AutoPulse is a multi-layer test automation framework — UI with Selenium and
Page Object Model, API testing with REST Assured, GitHub Actions CI/CD.
The differentiator is an AI failure analysis layer: when any test fails,
the system captures a screenshot, sends the failure context to Groq's
Llama 3.3 70B, and embeds a formatted root cause analysis (root cause, fix,
prevention) directly in the ExtentReport. I also built a Self-Healing Agent
that uses Groq's function calling to investigate locator failures — it
determines whether the failure is a stale locator or a real bug, and if
stale, suggests the exact replacement locator.

**"Why Groq over Gemini or OpenAI?":**
I evaluated providers. Groq runs on custom LPU hardware — fastest inference
available. Llama 3.3 70B supports function calling which was essential for
the agent loop. And it's completely free — 14,400 requests per day, no
billing. That's an engineering decision, not just API usage.

**"What's an AI agent vs an AI call?":**
An AI call is one shot in, one shot out — your code controls the flow.
An AI agent is different: you give the model a goal and a set of tools —
functions it can request to have run. The model decides what to do next
based on what it observes. The loop runs until the model gives a final
answer. The key insight is that the model never runs your code directly —
it emits a structured function call request, your code runs the real
function, and the result goes back into the conversation.

**"What was the hardest debugging session?":**
The AI layer took significant investigation across two problems. First,
Google AI Studio was generating AQ. format credentials — a restriction
triggered by an earlier key exposure on GitHub. None of the standard auth
methods worked. I diagnosed this by isolating variables with curl tests
and reading Google's specific error codes. Switched to Groq. Second,
Cognizant's Zscaler agent blocked all Generative AI category URLs on the
work laptop. I discovered this by comparing curl results between network
types, identified that Zscaler was doing SSL interception (causing PKIX
errors in Java), and fixed Maven with -Djavax.net.ssl.trustStoreType=
Windows-ROOT to use the Windows certificate store which already trusted
Zscaler's CA.

**"Why Page Object Model?":**
POM separates what to test from how to interact. Locators live in page
classes, not test classes. When UI changes, fix one locator in one place —
all tests using it automatically work again.

**"Why API testing if you have UI tests?":**
UI can look correct even when backend is broken. API tests also run 20x
faster — 300ms vs 8 seconds for the same scenario. Both layers are necessary.

---

## Key Principles Sandeep Has Internalized

1. Page Objects own WHAT. BasePage owns HOW.
2. Single Source of Truth — config values in one place
3. Separation of Concerns — every class has one job
4. DRY — Don't Repeat Yourself
5. Fallbacks never break the framework — AI failure returns a string
6. `alwaysRun=true` on all TestNG lifecycle methods when using groups
7. Test data isolation — dynamic emails with System.currentTimeMillis()
8. Main cannot see test code — AutoPulseListener in test folder
9. Never put secrets in URLs — use headers
10. Feature flags per environment — ai.enabled controls AI per machine
11. Every agent needs a leash — MAX_STEPS prevents infinite loops

---

## Important Technical Decisions

| Decision | Reason |
|---|---|
| Groq over Gemini | Google account restricted to AQ. tokens, Groq free + function calling |
| ai.enabled=false on work laptop | Zscaler blocks Groq, graceful degradation |
| GROQ_API_KEY env var name | Replaced GEMINI_API_KEY in ConfigReader and CI yml |
| Bearer header for Groq | Standard OpenAI-compatible auth |
| formatAsHtml() in FailureAnalyser | Owns AI response structure, not ExtentReportManager |
| CAUSE/FIX/PREVENT labels in prompt | Reliable parsing for HTML formatting |
| ThreadLocal<By> in BasePage | Tracks last locator per thread for agent |
| MAX_STEPS=6 in agent | Prevents infinite loop — every agent needs a leash |
| Keys.RETURN for search | All click mechanisms fail on Chrome 148 |
| EAGER page load strategy | automationexercise.com ads prevent NORMAL |
| alwaysRun=true on all lifecycle | Required for TestNG group filtering |

---

## Files That Are Gitignored

```
src/test/resources/config.properties
reports/
reports/screenshots/
target/
failures.json
~$*.xlsx
```

---

## Work Laptop Constraints

- Zscaler agent blocks all "Generative AI" URLs (Groq, OpenAI, etc.)
- Gemini passes Zscaler but AQ. key format is unusable
- Maven needs: `$env:MAVEN_OPTS="-Djavax.net.ssl.trustStoreType=Windows-ROOT"`
- All development on personal laptop
- Work laptop: git push/pull only

---

## How To Continue From Here

**You are on personal laptop. Groq AI is working.**

**Step 1 — Clone and set up (if not done):**
```bash
git clone https://github.com/sandeep-kaki/AutoPulse.git
cd AutoPulse
```
Create `src/test/resources/config.properties` with ai.enabled=true and real Groq key.

**Step 2 — Close Day 10 (do these in order):**
1. Fix SmokeTest.java — restore correct assertion
2. Fix UserApiTest.java — remove initReport() call
3. Add formatAsHtml() and extractLabel() to FailureAnalyser
4. Update buildPrompt() to use CAUSE/FIX/PREVENT format
5. Wire formatAsHtml() into analyse() return
6. Run `mvn test` — all tests green, full report, AI shows formatted analysis
7. Commit: `git commit -m "Day 10 complete: Groq AI layer, formatted analysis, all tests green"`

**Step 3 — Day 11: Self-Healing Agent**
Start with: "Let's start Day 11 Part 1 — Self-Healing Agent"
Claude will give the full concept explanation then SelfHealingAgent.java skeleton.

**Step 4 — Day 12: Final Polish + Interview Prep**

---

*Context generated from full conversation history. Personal laptop, Groq working,
Day 10 in progress. Pick up exactly here.*
