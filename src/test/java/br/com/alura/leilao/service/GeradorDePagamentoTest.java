package br.com.alura.leilao.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;

class GeradorDePagamentoTest {
	
	private GeradorDePagamento geradorDePagamento;
	
	@Mock
	private PagamentoDao pagamentoDao;
	
	@Captor
	private ArgumentCaptor<Pagamento> pagamentoCaptor;
	
	@BeforeEach
	void init() {
		MockitoAnnotations.initMocks(this);
		this.geradorDePagamento = new GeradorDePagamento(pagamentoDao);
	}

	@Test
	void test_deveriaCriarPagamentoParaVencedorDoLeilao() {
		var leilao = leilao();
		var lanceVencedor = leilao.getLanceVencedor();
		
		geradorDePagamento.gerarPagamento(lanceVencedor);
		
		verify(pagamentoDao).salvar(pagamentoCaptor.capture());
		
		var pagamento = pagamentoCaptor.getValue();
		
		assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento());
		assertTrue(pagamento.getUsuario().getNome().contains(lanceVencedor.getUsuario().getNome()));
		assertEquals(lanceVencedor.getValor(), pagamento.getValor());
		assertFalse(pagamento.getPago());
		assertEquals(leilao, pagamento.getLeilao());
	}
	
	private Leilao leilao() {
		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Teste"));
		Lance primeiroLance = new Lance(new Usuario("Beltrano"), new BigDecimal("600"));
		leilao.propoe(primeiroLance);
		leilao.setLanceVencedor(primeiroLance);
		return leilao;
	}

}
