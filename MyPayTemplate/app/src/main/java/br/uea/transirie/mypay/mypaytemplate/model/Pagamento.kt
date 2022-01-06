package br.uea.transirie.mypay.mypaytemplate.model

import androidx.room.*
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.*
import java.time.LocalDate

@Entity(tableName = TABLE_PAGAMENTO,
    foreignKeys = [
        ForeignKey(entity = Atendimento::class,
            parentColumns = [COLUMN_ID],
            childColumns = [COLUMN_ATENDIMENTO_ID])
    ])
data class Pagamento (
    @PrimaryKey(autoGenerate = true)
     @ColumnInfo(name = COLUMN_ID, index = true) var id: Long = 0,
     @ColumnInfo(name = COLUMN_ATENDIMENTO_ID) var atendimentoId: Long = 0,
     @ColumnInfo(name = COLUMN_ESTABELECIMENTO_CNPJ) var estabelecimentoCNPJ:String = "",
     @ColumnInfo(name = COLUMN_VALOR) var valor: Double = 0.0,
     @ColumnInfo(name = COLUMN_DATA_PAGAMENTO) var dataPagamento: String = "",
     @ColumnInfo(name = COLUMN_TIPO_PAGAMENTO) var tipoPagamento: TipoPagamento
){
    object dataHoje{
        var dataHoje: LocalDate = LocalDate.now()   // variável que armazena o total recebido no dia
    }
    object caixaInicial{
        var valorCaixaInicial: Float =0f
    }
    object totalClientes{
        var totalCliente: Int = 0 // variável que armazena o total de clientes do dia
    }
    object dinheiroRecebimento{
        var valorDinheiro: Float = 0f
    }
    object creditoRecebimento{
        var valorCredito: Float = 0f
    }
    object debitoRecebimento{
        var valorDebito: Float = 0f
    }
    object despesasCartao{
        var valorDespesasCartaoCredito: Float = 0f
        var valorDespesasCartaoDebito: Float = 0f
    }
    object despesasDinheiro{
        var valorDespesasDinheiroDia: Float = 0f
    }
    object finalizado{
        var finalizado:Boolean = false
    }
}