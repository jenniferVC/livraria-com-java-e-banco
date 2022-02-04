
package Models;


public class BillingInfo {
    //Billing Info - Informações de Pagamento
    private Account account;
    private String pagamento;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPagamento(String text) {
        this.pagamento = text;
    }
    
}
