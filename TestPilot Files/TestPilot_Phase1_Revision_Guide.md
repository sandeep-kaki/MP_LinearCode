# TestPilot — Phase 1 Full Revision
### One hour. Every concept. Every pattern. Locked in before Phase 2.

**How to use this:** Read a section's concept recap, answer the quick-fire questions out loud before checking them, then write the exercise code yourself before looking at the solution. Don't skip to solutions early — the whole point of revision is forcing recall, not re-reading.

**Suggested time budget (60 min total):**
| Section | Topic | Minutes |
|---|---|---|
| 1 | The Interpreter Mindset (Day 2) | 6 |
| 2 | Data Structures (Day 3) | 8 |
| 3 | Classes and OOP (Day 4) | 8 |
| 4 | Error Handling & Patterns (Day 5) | 10 |
| 5 | REST APIs & Function Calling (Phase 0 / Day 5) | 6 |
| 6 | Virtual Envs & Config (Day 6) | 8 |
| 7 | Git Discipline (today, live) | 4 |
| 8 | Final Integration Exercise | 10 |

---

## Section 1 — The Interpreter Mindset (Day 2)

**The core shift from Java:** Java compiles to bytecode first, then a JVM runs that bytecode — two distinct stages, and the compiler catches type errors before anything runs. Python has no compile stage you interact with — the interpreter reads and executes your file top to bottom, line by line, right now. This is *why* Python feels faster to iterate in (no build step) and *why* it can surprise you at runtime with errors Java's compiler would have caught up front (calling a method that doesn't exist, passing the wrong type). Speed of iteration, traded for a weaker safety net — which is exactly why type hints exist, to claw some of that safety back voluntarily.

**Indentation is the syntax, not decoration.** Java uses `{ }` to define a block; Python uses consistent indentation. Mixing tabs and spaces, or shifting indent level by one space, is a `IndentationError` — not a style complaint, a real syntax error.

**`None` vs `null`, `True`/`False` vs `true`/`false`** — same concepts, Python just capitalizes its booleans and calls its null-equivalent `None`.

**f-strings** — `f"Step {step_number}: {action}"` — Python's answer to `String.format()`, but the expression sits directly inside the braces rather than as a separate positional argument.

**Functions**: `def` has no return-type declaration (type hints are optional, not enforced by the interpreter), and Python supports **default arguments** (`def f(x, value="")`) and **keyword arguments** (`f(value="foo", x=1)` — order stops mattering once you name the argument) far more idiomatically than Java's method overloading ever did.

### Quick-fire
1. What happens if two lines inside the same block have inconsistent indentation?
2. Python is interpreted, not compiled — name one practical consequence for how confidently you can trust a script *before* running it, compared to Java.

### Exercise 1
Write a function `format_test_step(step_number, action, target, value="")` that returns a formatted string describing the step. Call it twice — once using keyword arguments, once relying on the default `value`.

---

## Section 2 — Data Structures (Day 3)

**The direct mapping to Java, so nothing here should feel foreign:**
- `list` → `ArrayList`
- `dict` → `HashMap`
- `tuple` → an immutable, fixed-size list (no true Java equivalent — closest is a `record` used purely as a bundle)
- `set` → `HashSet`

**List comprehensions** — `[x * 2 for x in range(5) if x % 2 == 0]` — are Python's answer to the Java stream-filter-map chain, but inline and readable in one line. The WHY: they're not just shorter, they signal intent immediately — "I am building a new list from this one" — versus a `for` loop where the intent is buried until you read the whole body.

**`dict.get()` vs `dict[]`** — this is a real trap, not a style choice. `my_dict["missing_key"]` throws a `KeyError` and crashes if the key isn't there. `my_dict.get("missing_key")` returns `None` safely (or a default you specify: `.get("key", "fallback")`). Use `.get()` whenever a key might legitimately be absent — like an optional `Value` column in a test step row.

**JSON module** — built into Python, no library needed (unlike Java, where you'd reach for Jackson or Gson). `json.dumps(dict)` → string, `json.loads(string)` → dict. This is the exact mechanism `runs_history.json` will use.

**`pathlib`** — Python's cleaner answer to Java's `File`. `Path("test_data") / "test_cases.xlsx"` builds a path with the `/` operator overloaded for joining — no manual string concatenation, no OS-specific separator bugs.

### Quick-fire
1. What's the difference in behavior between `my_dict.get("missing_key")` and `my_dict["missing_key"]` when the key doesn't exist?
2. Why is a list comprehension considered better practice than a manually written `for` loop that appends to a new list, beyond just being shorter?

### Exercise 2
Given this list of step dicts, write a **list comprehension** that returns only the steps where `"Expected"` is non-empty:
```python
steps = [
    {"Action": "navigate", "Target": "/", "Expected": "Page loads"},
    {"Action": "type", "Target": "username_field", "Expected": ""},
    {"Action": "click", "Target": "login_button", "Expected": ""},
    {"Action": "assert_visible", "Target": "product_page_header", "Expected": "Products page shown"},
]
```

### Exercise 3
Write a small dict representing a run summary to a JSON file using `pathlib`, then read it back and print one specific field. (This exercise **is** the run-history feature, just in miniature.)

---

## Section 3 — Classes and OOP (Day 4)

**`self` vs `this`** — Java's `this` is implicit; Python's `self` must be declared as the first parameter of every instance method, explicitly. WHY: Python favors "explicit is better than implicit" as a core design philosophy — you should always be able to see, right in the method signature, that this method operates on an instance.

**No `private` keyword.** Java enforces access control at compile time. Python has no enforcement at all — instead, a **convention**: `_single_underscore` signals "treat this as internal, don't touch it from outside," but nothing stops you from touching it anyway. This is a trust-based convention, not a wall.

**`@dataclass`** — Python's answer to a Java record or a plain data-holder POJO. It auto-generates `__init__`, `__repr__`, and equality comparison from your field declarations, so you never hand-write boilerplate constructors for simple data objects like `TestStep`.

**Inheritance**: `super().__init__()` calls the parent class's constructor — identical concept to Java's `super()`, same purpose (let the parent set up its own state before the child adds more).

**Composition vs inheritance** — inheritance means "IS-A" (LoginPage IS-A BasePage). Composition means "HAS-A" (an agent HAS-A GroqClient, rather than being one). Reach for composition when the relationship is "uses a capability," and inheritance when it's genuinely "is a specialized version of."

### Quick-fire
1. Why does Python require `self` to be written explicitly in every method signature, while Java hides `this`?
2. Given Python has no real `private` enforcement, what does the `_single_underscore` prefix actually communicate to another developer reading your code?

### Exercise 4
Write a `TestStep` dataclass with fields: `step_number: int`, `action: str`, `target: str`, `value: str`, `expected: str`.

### Exercise 5
Write a `BasePage` class with a stub `click(selector)` method that just prints what it would click. Then write `LoginPage(BasePage)` that calls `super().__init__()` and adds an `enter_username(value)` method.

---

## Section 4 — Error Handling & Python Patterns (Day 5)

**try/except vs try/catch** — same mechanism, different keyword. The real skill isn't the syntax, it's **specificity**. `except Exception:` catches *everything*, including bugs you didn't anticipate and genuinely want to see crash loudly during development. Catching a specific exception type (`except FileNotFoundError:`, `except InvalidFileException:`) means you only silently handle the failure you actually planned for — everything else still surfaces as a real crash with a real stack trace, which is what you want while debugging.

**`finally`** — same word, same guarantee: this block runs whether the `try` succeeded or an exception was raised. Used for cleanup that must always happen (closing a file handle, for instance).

**Custom exceptions — and the exact bug you already hit.** Two real traps live here, and you've personally run into both:
1. **Shadowing a library's real exception with your own class of the same name.** If openpyxl raises its own `InvalidFileException` and you also define a custom class called `InvalidFileException`, Python's name resolution can end up pointing at *your* empty class instead of the library's real one — your `except` block silently stops catching what you think it's catching. **Fix: always give custom exceptions a name that cannot collide** — `TestDataError`, not `InvalidFileException`.
2. **Discarding a validated object and reopening the source file in an incompatible mode.** If you validate a file exists, then throw away that check and re-open the file with the wrong mode (text mode for a binary `.xlsx`, for instance) instead of reusing the object you already validated, you've done the validation for nothing.

**Context managers (`with` statement)** — WHY they exist: guaranteed cleanup, even if an exception happens mid-block. `with open("file") as f:` guarantees the file closes, no matter what happens inside that block. This is *exactly* why Playwright's browser lifecycle will lean on this pattern — guaranteed browser cleanup even if a test step throws.

**Decorators** — `@property` lets you call a method like an attribute (`obj.status` instead of `obj.status()`), useful when a value is computed but should *read* like a simple field. `@staticmethod` marks a method that doesn't touch `self` or any instance state at all — it's just grouped inside the class for organizational reasons.

**Type hints** — `def analyse(self, step: TestStep) -> str:` — not enforced at runtime, but read by your IDE and by other developers as documentation of intent. Free correctness signal, zero runtime cost.

### Quick-fire
1. What's the practical downside of writing `except Exception:` everywhere, in terms of debugging six months from now?
2. In your own words — what actually goes wrong when you name a custom exception class the same as a real library exception class?

### Exercise 6
Write `safe_load_excel(path)` that raises a custom exception (correctly named — not shadowing anything) if the file doesn't exist, checking with `pathlib` *before* attempting to open it.

### Exercise 7
Write a simple context manager (using `@contextmanager` from `contextlib`) that prints `"Opening resource"` before and `"Closing resource"` after a block of code runs.

### Exercise 8
Go back to exercises 1, 4, 5, and 6 — add complete type hints to every function and method signature you wrote, including return types.

---

## Section 5 — REST APIs & Function Calling (Phase 0 / Day 5)

**REST calls in Python** — the `requests` library is the standard, simple, synchronous way to call any HTTP API: `requests.get(url)` returns a response object; `.json()` parses the body straight into a Python dict.

**Function calling / tool use — the concept that underlies TestPilot's entire architecture.** This is not magic, and it's not the AI "doing" anything physical. The model receives a description of available tools (name, parameters, what each does) alongside your prompt, and it returns **structured JSON** saying "call this tool with these arguments." That's the entire mechanism. Your Python code then reads that JSON and decides what to actually do with it.

**The three-layer model — say this cold, no notes:**
```
Human (Excel)  →  Agent Brain (Groq decides, returns JSON)  →  Playwright (Python executes it)
```
The surgeon analogy: the surgeon (Groq) decides what to cut. The surgeon does not cut with bare hands. The nurse (your Python code) reads the decision and hands over the right instrument. The scalpel (Playwright) does the actual cutting. The AI never touches the browser — it only ever returns a decision.

### Quick-fire
1. In your own words: what does Groq actually return when it "decides to call a tool"? (If your answer includes the words "it clicks the button," that's the misconception to correct.)
2. Why can the AI never call Playwright directly, architecturally?

### Exercise 9
Call `https://jsonplaceholder.typicode.com/todos/1` using `requests.get()`, parse the JSON response, and print just the `"title"` field from it — not the whole response.

---

## Section 6 — Virtual Environments & Config (Day 6)

**Why venv exists** — Maven gives you per-project dependency isolation automatically, building a project-specific classpath from your `pom.xml`. Python's global `pip` installs, by default, into one shared folder for your *entire machine* — no isolation at all unless you create it yourself. A `venv` is that isolation: a private, project-specific Python installation so two projects on the same laptop can depend on different versions of the same library without conflict.

**`requirements.txt` vs `pom.xml`** — same job (declare what the project depends on), radically simpler format: flat text, one package per line, no XML, no scopes.

**`.env` + `python-dotenv` vs `config.properties`** — identical purpose: keep secrets and environment-specific values out of source code, load them at runtime. `.env` never gets committed; `.env.example` is the committed template with placeholder values.

**`config.py` as single source of truth** — every other module reads config through this one file, never calling `os.getenv()` directly elsewhere. `@classmethod` shows up here for `validate()` because it operates on the class as a whole, not on any particular instance.

**The trap worth burning into memory**: **everything from `.env` arrives as a string.** `PAGE_LOAD_TIMEOUT=30` becomes the string `"30"`, not the integer `30` — always convert explicitly with `int(...)`. And the sharper trap: **`bool("false")` evaluates to `True` in Python**, because `bool()` on a non-empty string only checks "does this string exist," not what it says. Always compare the actual text: `.lower() == "true"`.

### Quick-fire
1. What does `bool(os.getenv("HEADLESS"))` evaluate to if `HEADLESS=false` is sitting in your `.env` file — and why?
2. Why do timeout values pulled from `.env` need an explicit `int(...)` conversion before you can do arithmetic with them?

### Exercise 10
Add one new config value of your choice to `config.py` — pull it from `.env`, convert it to the correct type, and give it a sane default.

---

## Section 7 — Git Discipline (today, live and real)

**`git commit` vs `git push`** — commit is 100% local, writing a snapshot into your machine's `.git` folder. Push is the separate act of syncing that history to GitHub. Nothing is backed up, visible, or shareable until it's pushed.

**Why `.gitignore` didn't stop `.env` from being committed** — `.gitignore` only blocks files Git hasn't started tracking yet. If a file gets `git add`-ed even once before `.gitignore` has the right line in it, Git is already tracking it — the ignore rule does nothing retroactively. Fix: `git rm --cached <file>` to untrack it while keeping it on disk, then `git commit --amend --no-edit` if it was your most recent commit.

### Quick-fire
1. If you add a file to `.gitignore` *after* it's already been committed once, does it disappear from history automatically?

### Exercise 11
Run `git check-ignore -v .env` in your project right now and read what it reports — this is your go-to diagnostic any time something isn't being ignored the way you expect.

---

## Section 8 — Final Integration Exercise (the capstone)

Write a single script, `revision_check.py`, that touches **every concept from this week** in one small program:
- A `@dataclass` (`TestStep`)
- A custom exception with a collision-safe name
- A hand-rolled context manager using `@contextmanager`
- `pathlib` for file existence and reading
- The `json` module for reading structured data
- A list comprehension to filter results
- Type hints throughout

If you can write this without looking anything up, Phase 1 is genuinely locked in — not just "read once," actually internalized.

---
---

# SOLUTIONS & SELF-CHECK

**Attempt everything above before reading past this line.**

### Section 1 answers
1. `IndentationError` — Python refuses to run, since indentation IS the block structure, not a style choice.
2. You get far less safety before runtime — a typo'd variable name or wrong-type argument won't be caught until that exact line executes, unlike Java's compiler catching it up front.

```python
def format_test_step(step_number: int, action: str, target: str, value: str = "") -> str:
    if value:
        return f"Step {step_number}: {action} on '{target}' with value '{value}'"
    return f"Step {step_number}: {action} on '{target}'"

print(format_test_step(step_number=1, action="type", target="username_field", value="standard_user"))
print(format_test_step(step_number=2, action="click", target="login_button"))
```

### Section 2 answers
1. `.get()` returns `None` (or your specified default) safely. `[]` raises a `KeyError` and crashes the program.
2. It's not just brevity — the comprehension signals "I'm building a new list from this one" immediately, at a glance, while a `for` loop's intent stays hidden until you've read the entire body including the `.append()` call at the bottom.

```python
steps = [
    {"Action": "navigate", "Target": "/", "Expected": "Page loads"},
    {"Action": "type", "Target": "username_field", "Expected": ""},
    {"Action": "click", "Target": "login_button", "Expected": ""},
    {"Action": "assert_visible", "Target": "product_page_header", "Expected": "Products page shown"},
]

steps_with_expectations = [s for s in steps if s["Expected"]]
print(steps_with_expectations)
```

```python
import json
from pathlib import Path

run_summary = {"run_id": "run_revision_test", "passed": 2, "failed": 0}

history_path = Path("runs_history_test.json")
history_path.write_text(json.dumps(run_summary, indent=2))

loaded = json.loads(history_path.read_text())
print(loaded["passed"])
```

### Section 3 answers
1. Explicit-is-better-than-implicit — Python wants you to see, directly in the signature, that a method binds to an instance. It never hides that binding behind implicit keyword magic the way Java does with `this`.
2. It communicates "this is internal — treat it as implementation detail, don't rely on it from outside this class" — a social contract between developers, not an enforced rule.

```python
from dataclasses import dataclass

@dataclass
class TestStep:
    step_number: int
    action: str
    target: str
    value: str
    expected: str
```

```python
class BasePage:
    def __init__(self, page):
        self.page = page

    def click(self, selector: str) -> None:
        print(f"[stub] clicking {selector}")


class LoginPage(BasePage):
    def __init__(self, page):
        super().__init__(page)

    def enter_username(self, value: str) -> None:
        print(f"[stub] typing '{value}' into username field")
```

### Section 4 answers
1. Six months later, when something breaks, you have zero idea what kind of failure actually happened — a `FileNotFoundError` and a `TypeError` both get silently swallowed by the same broad `except Exception:` block, and you lose the specific signal that would've told you where to look.
2. Python's name resolution ends up pointing at your (probably empty) custom class instead of the library's real one, so your `except` block quietly stops catching what the library actually raises — the exception you meant to catch slips right past your handler.

```python
from pathlib import Path

class TestDataError(Exception):
    """Raised when test data cannot be loaded. Deliberately not named like any openpyxl exception."""
    pass

def safe_load_excel(path: str) -> Path:
    file_path = Path(path)
    if not file_path.exists():
        raise TestDataError(f"Test data file not found at: {path}")
    return file_path
```

```python
from contextlib import contextmanager

@contextmanager
def resource():
    print("Opening resource")
    yield "resource_handle"
    print("Closing resource")

with resource() as handle:
    print(f"Using {handle}")
```

### Section 5 answers
1. It returns structured JSON — the tool's name plus the arguments to call it with — as plain text output from the model. It does not click, type, or touch a browser in any way; it only decides and describes what should happen.
2. Because the model has no hands — it's a text-in, text-out reasoning engine. Playwright requires an actual running browser process and a real DOM to interact with, which only your Python code has access to.

```python
import requests

response = requests.get("https://jsonplaceholder.typicode.com/todos/1")
data = response.json()
print(data["title"])
```

### Section 6 answers
1. `True` — `bool()` on any non-empty string only checks whether the string exists at all, not what it actually says. `"false"` is a non-empty string, so it's truthy. Always compare the text directly: `os.getenv("HEADLESS", "false").lower() == "true"`.
2. Every value pulled from `.env` arrives as a plain string, always — `"30"`, not `30`. Attempting `"30" + 5` raises a `TypeError`; you must convert explicitly once, in `config.py`, so every other module downstream just sees a clean `int`.

```python
# Add to config.py:
MAX_RETRIES: int = int(os.getenv("MAX_RETRIES", "3"))

# Add to .env and .env.example:
MAX_RETRIES=3
```

### Section 7 answer
1. No. `.gitignore` has no retroactive effect on history — once a file is committed, it stays in every snapshot from that point forward until you explicitly remove it with `git rm --cached` and rewrite or amend the commit.

### Section 8 — Capstone solution

```python
"""
revision_check.py
Capstone — every Phase 1 concept in one script.
"""

import json
from dataclasses import dataclass
from pathlib import Path
from contextlib import contextmanager


class ConfigValidationError(Exception):
    """Raised when required test data is missing or invalid."""
    pass


@dataclass
class TestStep:
    step_number: int
    action: str
    target: str
    value: str
    expected: str


@contextmanager
def load_json_file(path: str):
    file_path = Path(path)
    if not file_path.exists():
        raise ConfigValidationError(f"Missing data file: {path}")
    handle = file_path.open("r")
    try:
        yield json.load(handle)
    finally:
        handle.close()


def get_steps_with_expectations(steps: list[TestStep]) -> list[TestStep]:
    return [s for s in steps if s.expected]


if __name__ == "__main__":
    sample_data = [
        {"step_number": 1, "action": "navigate", "target": "/", "value": "", "expected": "Page loads"},
        {"step_number": 2, "action": "type", "target": "username_field", "value": "standard_user", "expected": ""},
    ]

    Path("sample_steps.json").write_text(json.dumps(sample_data))

    with load_json_file("sample_steps.json") as raw_steps:
        steps = [TestStep(**s) for s in raw_steps]

    meaningful_steps = get_steps_with_expectations(steps)
    print(f"Steps with expectations: {len(meaningful_steps)}")
    for step in meaningful_steps:
        print(step)
```

If you wrote something close to this on your own — congratulations, Phase 1 isn't just "done," it's actually yours now. That's the difference that matters going into Phase 2, where every one of these patterns shows up again inside real Playwright code.
