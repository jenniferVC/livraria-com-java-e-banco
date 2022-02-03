/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import Models.Account;
import Models.Author;
import Models.BillingInfo;
import Models.Book;
import Models.Customer;
import Models.Editorial;
import Models.Order;
import Models.OrderItens;
import Models.Publisher;
import Models.Shipper;
import Models.ShippingInfo;
import Models.User;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import tabelas_livraria.CreateTables;

public class LivrariaEXE {

    public static void main(String[] args) /*throws SQLException*/ {

        try {

            CreateTables tabelas = new CreateTables();
            //tabelas.create();

            Scanner scanner = new Scanner(System.in);

            // ---- Criando Autores ------
            Author a1 = new Author();
            a1.setName("C. S. Lewis");

            Author a2 = new Author();
            a2.setName("Sthephen King");

            Author a3 = new Author();
            a3.setName("Machado de Assis");

            DAOAuthor DA = new DAOAuthor();
            //DA.incluir(a1);
            //DA.incluir(a2);
            //DA.incluir(a3);

            //---- Criando Publicadoras ------
            Publisher p0 = new Publisher();
            p0.setName("Publicadora0");
            p0.setEndereco("Rua Ezequiel");

            Publisher p1 = new Publisher();
            p1.setName("Publicadora1");
            p1.setEndereco("Rua Galatos");

            DAOPublisher DP = new DAOPublisher();
            //DP.incluir(p0);
            //DP.incluir(p1);

            //---- Criando Livros ------
            Book b0 = new Book();
            b0.setTitulo("As Crônicas de Nárnia");
            b0.setAuthor(a1);
            b0.setEmail("lewis@gmail.com");
            b0.setPublisher(p0);

            Book b1 = new Book();
            b1.setTitulo("IT A Coisa");
            b1.setAuthor(a2);
            b1.setEmail("sthephen@gmail.com");
            b1.setPublisher(p1);

            Book b2 = new Book();
            b2.setTitulo("Dom Casmurro");
            b2.setAuthor(a3);
            b2.setEmail("casmurro@gmail.com");
            b2.setPublisher(p1);

            DAOBook DB = new DAOBook();
            //DB.incluir(b0);
            //DB.incluir(b1);
            //DB.incluir(b2);

            DAOUser DU = new DAOUser();

            DAOAccount DAC = new DAOAccount();

            System.out.println("\nQual o seu tipo de usuário?\n1 - Usuário Customer\n2 - Usuário Editorial");
            int tipouser = scanner.nextInt();

            // Usuário Customer
            if (tipouser == 1) {
                Customer userCustomer = new Customer();
                System.out.println("\nPossui uma conta?\n1 - SIM\n2 - NÃO");
                int temconta = scanner.nextInt();
                Account accountOld = new Account();
                Account accountNew = new Account();
                //SE tiver conta
                if (temconta == 1) {
                    System.out.println("Informe seu nome:");
                    String nome = scanner.next();
                    System.out.println("Insira o email da sua conta:");
                    String contaemail = scanner.next();
                    String emaail = DAC.consultarPorEmail(contaemail);
                    accountOld.setEmailAddress(contaemail);
                    System.out.println("Insira a senha da sua conta:");
                    String contapsw = scanner.next();
                    String psww = DAC.consultarPorPSW(contapsw);
                    accountOld.setPassword(contapsw);

                    if (emaail != "" && psww != "") {
                        User nomeusercostumerConsultar = DU.consultarPorNome(nome);
                        System.out.println("Login realizado com sucesso!");

                        System.out.println("\nGostaria de comprar livros? \n1 - SIM \n2 - NÃO  ");
                        int compr = scanner.nextInt();

                        //SE for comprar
                        if (compr == 1) {
                            System.out.println("Informe a data desta compra:");
                            String data = scanner.next();
                            System.out.println("Informe a forma do pagamento débito ou crédito:");
                            String pay = scanner.next();
                            //---- Criando Informações de Pagamento ------------
                            BillingInfo billinginfo = new BillingInfo();
                            DAOBillingInfo dbi = new DAOBillingInfo();

                            if (temconta == 1) {
                                billinginfo.setAccount(accountOld);
                                billinginfo.setPagamento(pay);
                                dbi.incluir(billinginfo);
                            } else {
                                billinginfo.setAccount(accountNew);
                                billinginfo.setPagamento(pay);
                                dbi.incluir(billinginfo);
                            }

                            System.out.println("Informe o endereço para a entrega:");
                            String adressEntrega = scanner.next();
                            //---- Criando Informações de Entrega ------------
                            ShippingInfo shippinginfo = new ShippingInfo();
                            shippinginfo.setAddress(adressEntrega);
                            //---- Definindo um Entregador ------
                            Shipper f = new Shipper();
                            f.setName("Flash");
                            shippinginfo.setShipper(f);

                            DAOShippingInfo dsi = new DAOShippingInfo();
                            dsi.incluir(shippinginfo);

                            //---- Criando Order ------------
                            Order compra = new Order();
                            compra.setDate(data);
                            compra.setUser(nomeusercostumerConsultar);
                            compra.setShippinginfo(shippinginfo);

                            DAOOrder daoorder = new DAOOrder();
                            daoorder.incluirOrder(compra);

                            System.out.println("------- Listando LIVROS ---------------- ");
                            List<Book> books = DB.listar("");
                            for (Book b : books) {
                                System.out.println("\n" + b.getBoid() + " - " + b.getTitulo());
                                System.out.println("Autor : " + b.getAuthor().getName());
                            }
                            int op = 1;

                            OrderItens orderitens = new OrderItens();

                            while (op == 1) {
                                System.out.println("\nEscolha a ID do livro que deseja: ");
                                int idbook = scanner.nextInt();
                                Book bookconsultado = DB.consultarPorID(idbook);

                                if (bookconsultado != null) { //SE o book consultado retornar um objeto que não seja nulo, adiciona na List Books na Classe Order
                                    orderitens.setOrder(compra);
                                    orderitens.setBooks(bookconsultado);
                                    System.out.println("Quantas unidades?");
                                    int qtde = scanner.nextInt();
                                    orderitens.setQtde(qtde);
                                    daoorder.incluirOrderItens(orderitens);
                                } else {
                                    System.out.println("Informe uma ID válida!");
                                }

                                System.out.println("Deseja escolher outro livro? \n1 - SIM \n2 - NÃO");
                                op = scanner.nextInt();

                            }
                        } else if (compr == 2) {
                            System.out.println("Volte sempre!");
                        } else {
                            System.out.println("Insira dados válidos!");
                        }
                    } else {
                        System.out.println("Algo deu errado! Tente novamente :)");
                    }
                } else if (temconta == 2) { //SE NÃO tem conta
                    System.out.println("\nInforme seu nome: ");
                    String nome = scanner.next();
                    userCustomer.setName(nome);
                    DU.incluir(userCustomer);

                    System.out.println("Cadastre um email:");
                    String criaremail = scanner.next();

                    System.out.println("Cadastre uma senha:");
                    String criarpsw = scanner.next();

                    userCustomer.setAccount(accountNew); //vinculando usuário com a conta
                    accountNew.setEmailAddress(criaremail);
                    accountNew.setPassword(criarpsw);
                    DAC.incluir(accountNew);

                } else {
                    System.out.println("Insira dados válidos!");
                }

            } else if (tipouser == 2) { //Editorial
                Editorial userEditorial = new Editorial();
                System.out.println("Insira o email da sua conta:");
                String contaemail = scanner.next();
                String emaail = DAC.consultarPorEmail(contaemail);

                System.out.println("Insira a senha da sua conta:");
                String contapsw = scanner.next();
                String psww = DAC.consultarPorPSW(contapsw);

                if (emaail != "" && psww != "") {
                    System.out.println("Login realizado com sucesso!");

                    System.out.println("\nO que deseja alterar?\n1 - Account\n2 - Author\n3 - BillingInfo\n4 - Books\n5 - Order\n6 - Publisher\n7 - ShippingInfo\n8 - User");
                    int opAltera = scanner.nextInt();

                    //DAO's para poder fazer as alterações no Banco
                    DAOAccount daoAccount = new DAOAccount();
                    DAOAuthor daoAuthor = new DAOAuthor();
                    DAOBillingInfo daoBillinginfo = new DAOBillingInfo();
                    DAOBook daoBook = new DAOBook();
                    DAOPublisher daoPublisher = new DAOPublisher();
                    DAOUser daoUser = new DAOUser();

                    switch (opAltera) {
                        case 1: //Account
                            System.out.println("--- Alteração da Account ---");
                            System.out.println("1 - Adicionar\n2 - Atualizar\n3 - Consultar\n4 - Excluir\n5 - Listar");
                            int opAc = scanner.nextInt();

                            switch (opAc) {
                                case 1: //adicionar
                                    System.out.println("Informe o email");
                                    String emailAdiciona = scanner.next();
                                    System.out.println("Informe a senha");
                                    String senhaAdiciona = scanner.next();

                                    Account accountAdicionar = new Account();
                                    accountAdicionar.setEmailAddress(emailAdiciona);
                                    accountAdicionar.setPassword(senhaAdiciona);
                                    daoAccount.incluir(accountAdicionar);
                                    break;
                                case 2: //atualizar
                                    System.out.println("Informe o email");
                                    String emailAtualiza = scanner.next();
                                    System.out.println("Informe a senha");
                                    String senhaAtualiza = scanner.next();
                                    System.out.println("Informe a ID da account");
                                    int idAtualiza = scanner.nextInt();

                                    Account accountAtualizar = new Account();
                                    accountAtualizar.setEmailAddress(emailAtualiza);
                                    accountAtualizar.setPassword(senhaAtualiza);
                                    accountAtualizar.setID(idAtualiza);
                                    daoAccount.alterar(accountAtualizar);
                                    break;
                                case 3: //consultar
                                    System.out.println("Informe o email");
                                    String emailConsulta = scanner.next();

                                    Account accountConsultar = new Account();
                                    accountConsultar.setEmailAddress(emailConsulta);
                                    daoAccount.consultarPorEmail(emailConsulta);
                                    break;
                                case 4: //Excluir
                                    System.out.println("Informe o id");
                                    int idExcluir = scanner.nextInt();

                                    Account accountExcluir = new Account();
                                    accountExcluir.setID(idExcluir);
                                    daoAccount.excluir(accountExcluir);
                                    break;
                                case 5: //Listar
                                    List<Account> accountlista = daoAccount.listar("");
                                    for (Account account : accountlista) {
                                        System.out.println("\n" + account.getID() + " - " + account.getEmailAddress());
                                        System.out.println("Senha : " + account.getPassword());
                                    }
                                    break;
                                default:
                                    System.out.println("Informe um valor válido!");
                            }
                            break;
                        case 2: //Author
                            System.out.println("--- Alteração da Author ---");
                            System.out.println("1 - Adicionar\n2 - Atualizar\n3 - Consultar\n4 - Excluir\n5 - Listar");
                            int opAu = scanner.nextInt();

                            switch (opAu) {
                                case 1: //adicionar
                                    System.out.println("Informe o nome");
                                    String nomeAdiciona = scanner.next();

                                    Author authorAdicionar = new Author();
                                    authorAdicionar.setName(nomeAdiciona);
                                    daoAuthor.incluir(authorAdicionar);
                                    break;
                                case 2: //atualizar
                                    System.out.println("Informe a id do author");
                                    int idauthorAtualiza = scanner.nextInt();
                                    System.out.println("Informe o nome");
                                    String nomeAtualiza = scanner.next();

                                    Author authorAtualizar = new Author();
                                    authorAtualizar.setAuid(idauthorAtualiza);
                                    authorAtualizar.setName(nomeAtualiza);
                                    daoAuthor.alterar(authorAtualizar);
                                    break;
                                case 3: //consultar
                                    System.out.println("Informe o nome");
                                    String nomeConsulta = scanner.next();

                                    Author authorConsultar = new Author();
                                    authorConsultar.setName(nomeConsulta);
                                    daoAuthor.consultarPorNome(authorConsultar);
                                    break;
                                case 4: //Excluir
                                    System.out.println("Informe o id");
                                    int idauthorExcluir = scanner.nextInt();

                                    Author authorExcluir = new Author();
                                    authorExcluir.setAuid(idauthorExcluir);
                                    daoAuthor.excluir(authorExcluir);
                                    break;
                                case 5: //Listar
                                    List<Author> authorlista = daoAuthor.listar("");
                                    for (Author author : authorlista) {
                                        System.out.println("\n" + author.getAuid() + " - " + author.getName());
                                    }
                                    break;
                                default:
                                    System.out.println("Informe um valor válido!");
                            }
                            break;
                        case 3: //BillingInfo
                            System.out.println("--- Alteração do BillingInfo ---");
                            System.out.println("1 - Adicionar\n2 - Atualizar\n3 - Consultar\n4 - Excluir\n5 - Listar");
                            int opBi = scanner.nextInt();

                            switch (opBi) {
                                case 1: //adicionar
                                    System.out.println("Informe o email:");
                                    String emailbiAdiciona = scanner.next();
                                    System.out.println("Informe a forma de pagamento:");
                                    String pagamentoAdiciona = scanner.next();

                                    Account accountbillinginfoAdiciona = new Account();
                                    accountbillinginfoAdiciona.setEmailAddress(emailbiAdiciona);

                                    BillingInfo billinginfoAdicionar = new BillingInfo();
                                    billinginfoAdicionar.setAccount(accountbillinginfoAdiciona);
                                    daoBillinginfo.incluir(billinginfoAdicionar);
                                    break;
                                case 2: //atualizar
                                    System.out.println("Informe o email:");
                                    String emailbiAtualiza = scanner.next();
                                    System.out.println("Informe a forma de pagamento:");
                                    String pagamentoAtualiza = scanner.next();

                                    daoBillinginfo.alterar(pagamentoAtualiza, emailbiAtualiza);
                                    break;
                                case 3: //consultar
                                    System.out.println("Informe o email a consultar:");
                                    String emailbiConsulta = scanner.next();

                                    daoBillinginfo.consultarPorEmail(emailbiConsulta);
                                    break;
                                case 4: //Excluir
                                    System.out.println("Informe o email que deseja excluir:");
                                    String emailbiExcluir = scanner.next();

                                    daoBillinginfo.excluir(emailbiExcluir);
                                    break;
                                case 5: //Listar
                                    List<BillingInfo> bilista = daoBillinginfo.listar("");
                                    for (BillingInfo bi : bilista) {
                                        System.out.println("\n" + bi.getAccount() + " - " + bi.getPagamento());
                                    }
                                    break;
                                default:
                                    System.out.println("Informe um valor válido!");
                            }
                            break;
                        case 4: //Books
                            System.out.println("--- Alteração do Books ---");
                            System.out.println("1 - Adicionar\n2 - Atualizar\n3 - Consultar\n4 - Excluir\n5 - Listar");
                            int opBook = scanner.nextInt();

                            switch (opBook) {
                                case 1: //adicionar
                                    System.out.println("Informe o titulo:");
                                    String tituloAdiciona = scanner.next();
                                    System.out.println("Informe o autor:");
                                    String autorAdiciona = scanner.next();
                                    System.out.println("Informe o email:");
                                    String emailboAdiciona = scanner.next();
                                    System.out.println("Informe o ID do Publisher:");
                                    String idpubliAdiciona = scanner.next();

                                    Author authorboAdiciona = new Author();
                                    authorboAdiciona.setName(autorAdiciona);
                                    daoAuthor.incluir(authorboAdiciona);

                                    Publisher publiboConsulta = daoPublisher.consultarPorID(idpubliAdiciona);

                                    Book bookAdiciona = new Book();
                                    if (publiboConsulta != null) {
                                        bookAdiciona.setTitulo(tituloAdiciona);
                                        bookAdiciona.setAuthor(authorboAdiciona);
                                        bookAdiciona.setEmail(emailboAdiciona);
                                        bookAdiciona.setPublisher(publiboConsulta);
                                        daoBook.incluir(bookAdiciona);
                                    } else {
                                        System.out.println("Informe um id de publisher válido!");
                                    }
                                    System.out.println("Livro adicionado com sucesso!");
                                    break;
                                case 2: //atualizar

                                    System.out.println("Informe a ID do Livro que deseja atualizar:");
                                    int idboAtualiza = scanner.nextInt();

                                    Book bookAtualizar = daoBook.consultarPorID(idboAtualiza);
                                    System.out.println("Informe o novo titulo do livro:");
                                    String tituloAtualiza = scanner.next();
                                    System.out.println("Informe o novo autor:");
                                    String autorAtualiza = scanner.next();
                                    Author authorboAtualiza = new Author();
                                    authorboAtualiza.setName(autorAtualiza);
                                    daoAuthor.incluir(authorboAtualiza);

                                    System.out.println("Informe o novo email:");
                                    String emailboAtualiza = scanner.next();
                                    System.out.println("Informe o ID do Publisher:");
                                    String idpubliAtualiza = scanner.next();

                                    Publisher publiboAtualiza = daoPublisher.consultarPorID(idpubliAtualiza);

                                    bookAtualizar.setTitulo(tituloAtualiza);
                                    bookAtualizar.setAuthor(authorboAtualiza);
                                    bookAtualizar.setEmail(emailboAtualiza);
                                    bookAtualizar.setPublisher(publiboAtualiza);
                                    daoBook.alterar(bookAtualizar);

                                    System.out.println("Livro atualizado com sucesso!");
                                    break;
                                case 3: //consultar
                                    System.out.println("Informe a ID do Livro:");
                                    int idboConsulta = scanner.nextInt();

                                    Book bookconsultado = daoBook.consultarPorID(idboConsulta);
                                    System.out.println(bookconsultado.getBoid() + " - " + bookconsultado.getAuthor().getName());
                                    // System.out.println("Autor: "+ bookconsultado.getAuthor().getName());
                                    break;
                                case 4: //Excluir
                                    System.out.println("Informe a ID do Livro que deseja excluir:");
                                    int idboExcluir = scanner.nextInt();
                                    daoBook.excluir(idboExcluir);
                                    System.out.println("Livro excluido com sucesso!");
                                    break;
                                case 5: //Listar
                                    List<Book> booklista = daoBook.listar("");
                                    for (Book bi : booklista) {
                                        System.out.println("\n" + bi.getBoid() + " - " + bi.getTitulo());
                                        System.out.println("Autor : " + bi.getAuthor().getName());
                                    }
                                    break;
                                default:
                                    System.out.println("Informe um valor válido!");
                            }
                            break;
                        case 8: //User
                            System.out.println("--- Alteração de User ---");
                            System.out.println("1 - Adicionar\n2 - Atualizar\n3 - Consultar\n4 - Excluir\n5 - Listar");
                            int opUs = scanner.nextInt();

                            switch (opUs) {
                                case 1: //adicionar
                                    System.out.println("Informe o nome");
                                    String nomeAdiciona = scanner.next();

                                    User userAdicionar = new User();
                                    userAdicionar.setName(nomeAdiciona);
                                    daoUser.incluir(userAdicionar);
                                    System.out.println("Usuário adicionado com sucesso!");
                                    break;
                                case 2: //atualizar
                                    System.out.println("Informe o nome de usuário que deseja atualizar:");
                                    String nomeAtualiza = scanner.next();

                                    User nomeuserAtualiza = daoUser.consultarPorNome(nomeAtualiza);
                                    System.out.println(nomeuserAtualiza.getUsid() + " - " + nomeuserAtualiza.getName());

                                    System.out.println("\nInforme o novo nome de usuário:");
                                    String nomeusAtualiza = scanner.next();
                                    nomeuserAtualiza.setName(nomeusAtualiza);
                                    daoUser.alterar(nomeuserAtualiza);
                                    System.out.println("Usuário atualizado com sucesso!");
                                    break;
                                case 3: //consultar
                                    System.out.println("Informe o nome");
                                    String nomeConsulta = scanner.next();

                                    User nomeuserConsulta = daoUser.consultarPorNome(nomeConsulta);
                                    System.out.println(nomeuserConsulta.getUsid() + " - " + nomeuserConsulta.getName());

                                    break;
                                case 4: //Excluir
                                    System.out.println("Informe a ID do usuário que deseja excluir:");
                                    int iduserExcluir = scanner.nextInt();
                                    System.out.println("Informe a ID da order que este usuário está vinculado:");
                                    int idorderExcluir = scanner.nextInt();
                                    daoUser.excluir(iduserExcluir, idorderExcluir);
                                    System.out.println("Usuário excluido com sucesso!");
                                    break;
                                case 5: //Listar
                                    List<User> userlista = daoUser.listar("");
                                    for (User bi : userlista) {
                                        System.out.println("\n" + bi.getUsid() + " - " + bi.getName());
                                    }
                                    break;
                                default:
                                    System.out.println("Informe um valor válido!");
                            }
                            break;
                        default:
                            System.out.println("Informe um valor válido!");
                    }
                } else {
                    System.out.println("Login ou senha inválidos! Tente novamente :)");
                }
            } else {
                System.out.println("Insira dados válidos!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
