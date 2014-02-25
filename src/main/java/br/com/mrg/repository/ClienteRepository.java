package br.com.mrg.repository;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.hibernate.SQLQuery;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import br.com.mrg.entity.Cliente;

@Stateless(name = "ClienteRepository")
@Local(IClienteRepository.class)
@SuppressWarnings("unchecked")
public class ClienteRepository extends DefaultRepository<Cliente> implements IClienteRepository {

	@Override
	public Cliente obterPorCnpj(String string) {
		return findById(string);
	}
	
	@Override
	public List<String> obterEstadosDaMarca(String marca) {
		SQLQuery query = createSession().createSQLQuery("SELECT DISTINCT estado FROM v_onde_comprar_clientes WHERE marcas LIKE :marca AND estado IS NOT NULL ");
		query.setParameter("marca", new StringBuilder().append("%").append(marca).append("%").toString());
		return query.list();
	}

	@Override
	public List<String> obterCidadesDaMacaEstado(String marca, String estado) {
		SQLQuery query = createSession().createSQLQuery("SELECT DISTINCT cidade FROM v_onde_comprar_clientes WHERE marcas LIKE :marca AND estado = :estado");
		query.setParameter("marca", new StringBuilder().append("%").append(marca).append("%").toString());
		query.setParameter("estado", estado);
		return query.list();
	}

	@Override
	public List<Cliente> obterClientesDaMarcaEstadoCidade(String marca, String estado, String cidade) {
		return createCriteria().add(Restrictions.eq("estado", estado)).add(Restrictions.eq("cidade", cidade)).add(Restrictions.ilike("marcas", marca, MatchMode.ANYWHERE)).list();
	}

	@Override
	public List<Cliente> obterClientesProximos(String marca, String lat, String lon) {
		StringBuilder sql = new StringBuilder("select * from ( select ");
		sql.append(" descricaoloja, latitude, longitude, cnpj, nomeloja, email, cidade, estado, endereco, bairro, complemento, cep, telefone, marcas, foto, ");
		sql.append(" TO_NUMBER(CEIL(6371 * acos(cos(degree2radian( replace(:lat, '.',',') )) * cos(degree2radian( REPLACE(LATITUDE,'.', ',') ))   * cos(degree2radian( REPLACE(LONGITUDE,'.', ',') ) -");
		sql.append(" degree2radian( replace(:lon, '.',',') )) + sin(degree2radian( replace(:lat, '.',',') )) * sin(degree2radian( REPLACE(LATITUDE,'.', ',') )))))  AS DISTANCIA");
		sql.append(" from v_onde_comprar_clientes where marcas like :marca) t WHERE DISTANCIA < 10");
		
		SQLQuery query = createSession().createSQLQuery(String.valueOf(sql));
		
		query.setParameter("marca", new StringBuilder().append("%").append(marca).append("%").toString());
		query.setParameter("lat", lat);
		query.setParameter("lon", lon);
		
		query.addScalar("cnpj", StringType.INSTANCE);
		query.addScalar("descricaoLoja", StringType.INSTANCE);
		query.addScalar("latitude", StringType.INSTANCE);
		query.addScalar("longitude", StringType.INSTANCE);
		query.addScalar("nomeLoja", StringType.INSTANCE);
		query.addScalar("email", StringType.INSTANCE);
		query.addScalar("cidade", StringType.INSTANCE);
		query.addScalar("estado", StringType.INSTANCE);
		query.addScalar("endereco", StringType.INSTANCE);
		query.addScalar("bairro", StringType.INSTANCE);
		query.addScalar("complemento", StringType.INSTANCE);
		query.addScalar("cep", StringType.INSTANCE);
		query.addScalar("telefone", StringType.INSTANCE);
		query.addScalar("marcas", StringType.INSTANCE);
		query.addScalar("distancia", IntegerType.INSTANCE);
		query.addScalar("foto", StringType.INSTANCE);
		
		return query.list();
	}

}