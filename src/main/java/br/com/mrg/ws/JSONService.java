package br.com.mrg.ws;

import java.util.List;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.mrg.entity.Cliente;
import br.com.mrg.repository.IClienteRepository;

import com.google.common.collect.Lists;

//http://127.0.0.1:8090/ws/clientes/MOR/PR/CIANORTE
@Path("/clientes") 
public class JSONService { 
	
	private static final String RETORNO_JSON = MediaType.APPLICATION_JSON + ";charset=UTF-8";
	
	@EJB
	private IClienteRepository clienteRepository;
	private Context ctx;
	
    @GET
    @Produces(RETORNO_JSON)
    @Path("/{marca}")
    public Response getEstados(@PathParam("marca") String marca) {
    	List<String> estados = Lists.newArrayList();
    	try {
			this.ctx = new InitialContext();
			this.clienteRepository = (IClienteRepository) this.ctx.lookup("java:global/prime-ejb-crud/ClienteRepository");  
			estados = clienteRepository.obterEstadosDaMarca(marca);
			return Response.ok().type(RETORNO_JSON).entity(estados).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(404).build();
		}  
    }
    
    @GET
    @Produces(RETORNO_JSON)
    @Path("/{marca}/{estado}")
    public Response getCidades(@PathParam("marca") String marca, @PathParam("estado") String estado) {
    	List<String> cidades = Lists.newArrayList();
    	try {
    		this.ctx = new InitialContext();
			this.clienteRepository = (IClienteRepository) this.ctx.lookup("java:global/prime-ejb-crud/ClienteRepository");
			cidades = this.clienteRepository.obterCidadesDaMacaEstado(marca, estado);
    		return Response.ok().type(RETORNO_JSON).entity(cidades).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(404).build();
		}
    }
    
    @GET
    @Produces(RETORNO_JSON)
    @Path("/{marca}/{estado}/{cidade}")
    public Response getClientes(@PathParam("marca") String marca, @PathParam("estado") String estado, @PathParam("cidade") String cidade) {
    	List<Cliente> cliente = Lists.newArrayList();
    	try {
    		this.ctx = new InitialContext();
			this.clienteRepository = (IClienteRepository) this.ctx.lookup("java:global/prime-ejb-crud/ClienteRepository");  
			cliente = this.clienteRepository.obterClientesDaMarcaEstadoCidade(marca, estado, cidade);
    		return Response.ok().type(RETORNO_JSON).entity(cliente).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(404).build();
		}
    }
    
    @GET
    @Produces(RETORNO_JSON)
    @Path("/proximo/{marca}/{lat}/{lon}")
    public Response getClientesProximo(@PathParam("marca") String marca, @PathParam("lat") String lat, @PathParam("lon") String lon) {
    	List<Cliente> clientesProximos = Lists.newArrayList();
    	try {
    		this.ctx = new InitialContext();
			this.clienteRepository = (IClienteRepository) this.ctx.lookup("java:global/prime-ejb-crud/ClienteRepository");  
			clientesProximos = this.clienteRepository.obterClientesProximos(marca, lat, lon);
    		return Response.ok().type(RETORNO_JSON).entity(clientesProximos).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(404).build();
		}
    }

//	@POST
//	@Path("/post")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response createTrackInJSON(String track) {
//		String result = "Track saved : " + track;
//		System.out.println(result);
//		return Response.status(201).entity(result).build();
//		
//	}
	
}