package br.com.mrg.repository;

import java.util.List;

import br.com.mrg.entity.Cliente;

public interface IClienteRepository extends IDefaultRepository<Cliente> {

	Cliente obterPorCnpj(String string);

	List<String> obterEstadosDaMarca(String marca);

	List<String> obterCidadesDaMacaEstado(String marca, String estado);

	List<Cliente> obterClientesDaMarcaEstadoCidade(String marca, String estado, String cidade);

	List<Cliente> obterClientesProximos(String marca, String lat, String lon);

}