package lab.mockito.basic.stub;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Scope("session")
public class AjaxController {
	private final CountryDao countryDao;

	public AjaxController(CountryDao countryDao) {
		this.countryDao = countryDao;
	}

	@ResponseBody
	@PostMapping(value = "retrieveCountries")
	public JsonDataWrapper<Country> retrieve(HttpServletRequest httpRequest) {

		QueryCountryRequest daoRequest = RequestBuilder.build(httpRequest);
		List<Country> countries = countryDao.retrieve(daoRequest);

		int size = countries.size();

		// 第~頁
		int page = daoRequest.getPage();
		// 每頁筆數
		int rowPerPage = daoRequest.getRowPerPage();
		// 資料擷取開始
		int startIndex = (page - 1) * rowPerPage;
		// 資料擷取結束
		int endIndex = (startIndex + rowPerPage) > size ? size : (startIndex + rowPerPage);

		if (startIndex < endIndex) {
			countries = countries.subList(startIndex, endIndex);
		}

		JsonDataWrapper<Country> wrapper = new JsonDataWrapper<>(page, size, countries);
		return wrapper;
	}
}
