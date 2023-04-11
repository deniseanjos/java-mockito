package leilao;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.com.alura.leilao.dao.LeilaoDao;

public class HelloWorldMockito {
	
	@Test
	void hello() {
		var mockLeilaoDao = Mockito.mock(LeilaoDao.class);
		var returnList = mockLeilaoDao.buscarTodos();
		assertTrue(returnList.isEmpty());
	}

}
