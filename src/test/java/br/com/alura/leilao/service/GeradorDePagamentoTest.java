package br.com.alura.leilao.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;

class GeradorDePagamentoTest {

	private GeradorDePagamento gerador;
	
	@Mock
	private PagamentoDao pagamentoDao;
	
	//Pega objetos internos ao metodo e simula
	@Captor
	private ArgumentCaptor<Pagamento> captor;
	
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
		this.gerador = new GeradorDePagamento(pagamentoDao);
	}
	
	@Test
	void deveriaCriarPagamentoParaVecedorDoLeilao() {
		Leilao leilao = leilao();
		Lance vencedor = leilao.getLanceVencedor();
		gerador.gerarPagamento(vencedor);
		//captor.capture() nesse caso está simulando um new Pagamento() com os devidos parametros.
		Mockito.verify(pagamentoDao).salvar(captor.capture());
		
		Pagamento pagamento = captor.getValue();
		Assertions.assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento());
		Assertions.assertEquals(vencedor.getValor(), pagamento.getValor());
		Assertions.assertFalse(pagamento.getPago());
		Assertions.assertEquals(vencedor.getUsuario(), pagamento.getUsuario());
		Assertions.assertEquals(leilao, pagamento.getLeilao());
	}
	
	private Leilao leilao() {

		Leilao leilao = new Leilao("Celular",
				new BigDecimal("500"),
				new Usuario("Fulano"));
		
		Lance lance = new Lance(new Usuario("Beltrano"),
				new BigDecimal("900"));

		leilao.propoe(lance);
		leilao.setLanceVencedor(lance);
		return leilao;
	}

}
