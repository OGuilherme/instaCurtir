package demo;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class Utils {

	private final static String urlInsta = "https://www.instagram.com/accounts/login/";
	private final static String urlInstaBase = "https://www.instagram.com/";
	private static int curtidasTotais = 0;
	private static int perfisTotais = 0;

	public static void logar(WebDriver driver) {
		Boolean isValid = false;
		driver.get(urlInsta);
		try {
			Thread.sleep(3000);
			while (!isValid) {
				if (!urlInsta.equalsIgnoreCase(driver.getCurrentUrl())) {
					isValid = true;
				}
			}
			Thread.sleep(3000);
			System.out.println("Logou com sucesso");
		} catch (Exception e) {
			System.out.println("Erro ao tentar logar./n" + e.getMessage());
		}
	}

	public static Integer curtidores(WebDriver driver, String urlFoto, int totalCurtidas) {
		curtidasTotais = totalCurtidas;
		driver.get(urlFoto);
		try {
			verSeguidores(driver);
			List<WebElement> pessoas = driver.findElements(By.cssSelector(".FPmhX.notranslate.MBL3Z"));
			int numPessoas = pessoas.size() - 1;
			Set<String> users = getAllNamesPicture(driver);
			for (String user : users) {
				if (curtidasTotais > 1500) {
					throw new Exception();
				}
				try {
					driver.get(urlInstaBase + user);
					Thread.sleep(1000);
					if (!perfilPublico(driver)) {
						driver.findElement(By.cssSelector(".v1Nh3.kIKUG._bz0w a")).click();
						Thread.sleep(2000);
						if (possivelCurtir(driver)) {
							driver.findElement(By.cssSelector(".ltpMr.Slqrh .QBdPU")).click();
							curtidasTotais++;
							Thread.sleep(300);
						}
					}
				} catch (Exception e) {
					System.out.println("erro ao curtir" + e);
				} finally {
					System.out.println(curtidasTotais + ", quantidade perfis:" + perfisTotais);
				}
			}
			perfisTotais += numPessoas;
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			System.out.println("curtidas: " + curtidasTotais + " e perfis: " + perfisTotais);
		}
		return curtidasTotais;
	}

	public static Set<String> getAllNamesPicture(WebDriver driver) {
		Set<String> users = new HashSet<String>();
		Integer acabouUsers = 0;
		Integer numUsers = 0;
		try {
			Thread.sleep(1500);

			JavascriptExecutor js = (JavascriptExecutor) driver;

			URL jqueryUrl = Resources.getResource("jquery.min.js");
			String jqueryText = Resources.toString(jqueryUrl, Charsets.UTF_8);
			js.executeScript(jqueryText);
			for (int index = 0; index < 1010; index++) {
				if (acabouUsers >= 2)
					break;
				js.executeScript("$('.FPmhX.notranslate.MBL3Z').focus()");

				Thread.sleep(5000);
				List<WebElement> test = driver.findElements(By.cssSelector(".FPmhX.notranslate.MBL3Z"));
				for (WebElement webElement : test) {
					users.add(webElement.getAttribute("title"));
				}
				if (numUsers == users.size()) {
					acabouUsers++;
				}
				numUsers = users.size();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return users;
	}

	public static void verSeguidores(WebDriver driver) {
		try {
			Thread.sleep(1500);
			List<WebElement> menus = driver.findElements(By.cssSelector(".Nm9Fw .sqdOP.yWX7d._8A5w5"));
			if (menus.size() > 0) {
				menus.get(0).click();
			}
		} catch (Exception e) {
			System.out.println(System.lineSeparator() + e.getMessage());
		}
	}

	public static Boolean possivelCurtir(WebDriver driver) {
		try {
			driver.findElement(By.className("FY9nT"));
			return false;
		} catch (Exception e) {
			return true;
		}
	}

	public static Boolean perfilPublico(WebDriver driver) {
		try {
			driver.findElement(By.cssSelector(".v1Nh3.kIKUG._bz0w"));
			return false;
		} catch (Exception e) {
			return true;
		}
	}
}