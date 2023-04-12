package br.com.alura.leilao.service;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

class FinalizarLeilaoServiceTest {

	private FinalizarLeilaoService service;

	@Mock
	LeilaoDao leilaoDao;
	
	@Mock
	EnviadorDeEmails enviadorDeEmails;

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
		this.service = new FinalizarLeilaoService(leilaoDao, enviadorDeEmails);
	}

	@Test
	void test_deveriaFinalizarUmLeilaoEEnviarEmail() {
		var leiloes = leiloes();
		assertTrue(leiloes.size() == 1);
		
		Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);
		
		service.finalizarLeiloesExpirados();
		
		var leilao = leiloes.get(0);
		
		assertTrue(leilao.isFechado());
		assertTrue(leilao.getLanceVencedor().getValor().equals(new BigDecimal("900")));
		
		Mockito.verify(leilaoDao).salvar(leilao);
		
		Mockito.verify(enviadorDeEmails).enviarEmailVencedorLeilao(leilao.getLanceVencedor());
	}

	private List<Leilao> leiloes() {
		List<Leilao> leiloes = new ArrayList<>();
		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Teste"));
		Lance primeiroLance = new Lance(new Usuario("Beltrano"), new BigDecimal("600"));
		Lance segundoLance = new Lance(new Usuario("Ciclano"), new BigDecimal("900"));
		leilao.propoe(primeiroLance);
		leilao.propoe(segundoLance);
		leiloes.add(leilao);
		return leiloes;
	}

}
