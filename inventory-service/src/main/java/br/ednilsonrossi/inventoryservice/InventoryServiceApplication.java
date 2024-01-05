package br.ednilsonrossi.inventoryservice;

import br.ednilsonrossi.inventoryservice.model.Inventory;
import br.ednilsonrossi.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }


    /*
     * O Bean criado a seguir carrega no banco de dados dois registros, contudo neste exemplo utiliza-se
     * um banco de dados H2 em memória, de forma que os dados sempre são apagados quando o serviço para.
     * Na implementação de um banco de dados real o código abaixo irá carregar dados repetidos na base
     * a cada execução do serviço.
     */
    @Bean
    public CommandLineRunner loadInventoryData(InventoryRepository repository){
        return args -> {
            Inventory iphone = new Inventory();
            iphone.setSkuCode("iphone_15");
            iphone.setQuantity(100);

            Inventory galaxy = new Inventory();
            galaxy.setSkuCode("galaxy_s23");
            galaxy.setQuantity(0);

            repository.save(iphone);
            repository.save(galaxy);
        };
    }

}