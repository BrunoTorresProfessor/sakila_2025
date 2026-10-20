package br.com.sakila;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import br.com.sakila.model.LanguageModel;
import br.com.sakila.repository.LanguageRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LanguageSeleniumTests {

	private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);

	@LocalServerPort
	private int port;

	@Autowired
	private LanguageRepository languageRepository;

	private WebDriver driver;
	private WebDriverWait wait;
	private String includedName;
	private String updatedName;

	@BeforeEach
	void openBrowser() {
		String suffix = Long.toString(System.nanoTime(), 36);
		includedName = "Sel-" + suffix;
		updatedName = "Edit-" + suffix;

		ChromeOptions options = new ChromeOptions();
		options.addArguments(
				"--disable-gpu",
				"--no-sandbox",
				"--disable-dev-shm-usage",
				"--window-size=1280,900");
		if (Boolean.parseBoolean(System.getProperty("selenium.headless", "true"))) {
			options.addArguments("--headless=new");
		}

		driver = new ChromeDriver(options);
		wait = new WebDriverWait(driver, WAIT_TIMEOUT);
	}

	@AfterEach
	void closeBrowserAndRemoveTestData() {
		if (driver != null) {
			driver.quit();
		}

		if (includedName != null && updatedName != null) {
			List<LanguageModel> testLanguages = languageRepository.findAll().stream()
					.filter(language -> includedName.equals(language.getName())
							|| updatedName.equals(language.getName()))
					.toList();
			if (!testLanguages.isEmpty()) {
				languageRepository.deleteAll(testLanguages);
				languageRepository.flush();
			}
		}
	}

	@Test
	void shouldIncludeUpdateAndDeleteLanguage() {
		driver.get(baseUrl("/languages"));

		includeLanguage();
		updateLanguage();
		deleteLanguage();
	}

	private void includeLanguage() {
		WebElement nameInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("name")));
		nameInput.sendKeys(includedName);
		driver.findElement(By.cssSelector("#language-form button[type='submit']")).click();

		waitForSuccessMessage("Idioma incluído com sucesso.");
		assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(rowByName(includedName))).isDisplayed());
		assertTrue(languageExists(includedName), "O idioma incluído deve estar persistido no banco.");
		pauseForVisualExecution();
	}

	private void updateLanguage() {
		WebElement includedRow = driver.findElement(rowByName(includedName));
		scrollAndClick(includedRow.findElement(By.linkText("Editar")));

		WebElement nameInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("name")));
		assertEquals(includedName, nameInput.getAttribute("value"));
		nameInput.clear();
		nameInput.sendKeys(updatedName);
		driver.findElement(By.cssSelector("#language-form button[type='submit']")).click();

		waitForSuccessMessage("Idioma atualizado com sucesso.");
		assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(rowByName(updatedName))).isDisplayed());
		assertFalse(languageExists(includedName), "O nome anterior não deve permanecer no banco.");
		assertTrue(languageExists(updatedName), "A alteração deve estar persistida no banco.");
		pauseForVisualExecution();
	}

	private void deleteLanguage() {
		WebElement updatedRow = driver.findElement(rowByName(updatedName));
		scrollAndClick(updatedRow.findElement(By.cssSelector("button[data-bs-toggle='modal']")));

		WebElement confirmationModal = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector("#delete-language-modal.show")));
		assertTrue(confirmationModal.findElement(By.id("delete-modal-description")).getText().contains(updatedName));
		pauseForVisualExecution();
		confirmationModal.findElement(By.id("confirm-delete-button")).click();

		waitForSuccessMessage("Idioma excluído com sucesso.");
		assertTrue(wait.until(ExpectedConditions.invisibilityOfElementLocated(rowByName(updatedName))));
		assertFalse(languageExists(updatedName), "O idioma excluído não deve permanecer no banco.");
		pauseForVisualExecution();
	}

	private void waitForSuccessMessage(String expectedMessage) {
		wait.until(ExpectedConditions.textToBePresentInElementLocated(
				By.cssSelector(".alert-success"), expectedMessage));
	}

	private boolean languageExists(String name) {
		return languageRepository.findAll().stream()
				.anyMatch(language -> name.equals(language.getName()));
	}

	private void scrollAndClick(WebElement element) {
		wait.until(ExpectedConditions.visibilityOf(element));
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", element);
	}

	private void pauseForVisualExecution() {
		long delay = Long.getLong("selenium.step-delay-ms", 0L);
		if (delay <= 0) {
			return;
		}

		try {
			Thread.sleep(delay);
		} catch (InterruptedException exception) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException("A execução visual do Selenium foi interrompida.", exception);
		}
	}

	private By rowByName(String name) {
		return By.xpath("//tbody/tr[td[normalize-space()=" + xpathLiteral(name) + "]]");
	}

	private String xpathLiteral(String value) {
		return "'" + value.replace("'", "") + "'";
	}

	private String baseUrl(String path) {
		return "http://localhost:" + port + path;
	}
}
