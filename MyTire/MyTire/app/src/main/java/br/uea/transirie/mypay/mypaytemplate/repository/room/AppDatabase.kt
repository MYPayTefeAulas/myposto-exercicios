package br.uea.transirie.mypay.mypaytemplate.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.uea.transirie.mypay.mypaytemplate.model.*
import br.uea.transirie.mypay.mypaytemplate.repository.room.converters.TipoPagamentoConverter
import br.uea.transirie.mypay.mypaytemplate.repository.room.dao.*
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.COLUMN_DESATIVADO
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.DATABASE_NAME
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.DATABASE_VERSION
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.TABLE_SERVICO
import org.jetbrains.anko.doAsync

@Database(entities = [
    Servico::class,
    Pagamento::class,
    ItemAtendimento::class,
    Estabelecimento::class,
    Cliente::class,
    Atendimento::class,
    Veiculo::class,
    Despesa::class,
    Usuario::class
], version = DATABASE_VERSION, exportSchema = false)
@TypeConverters(TipoPagamentoConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun servicoDao(): ServicoDao
    abstract fun pagamentoDao(): PagamentoDao
    abstract fun itemAtendimentoDao(): ItemAtendimentoDao
    abstract fun estabelecimentoDao(): EstabelecimentoDao
    abstract fun clienteDao(): ClienteDao
    abstract fun atendimentoDao(): AtendimentoDao
    abstract fun veiculoDao(): VeiculoDao
    abstract fun despesaDao(): DespesaDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        private var instance: AppDatabase? = null

        const val migrationQuery_1_2 =
                "ALTER TABLE $TABLE_SERVICO ADD COLUMN $COLUMN_DESATIVADO INTEGER DEFAULT 0 NOT NULL"
        private val MIGRATION_1_2 = object: Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(migrationQuery_1_2)
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            if(instance == null) {
                synchronized(this) {
                    instance = Room
                            .databaseBuilder(
                                    context.applicationContext,
                                    AppDatabase::class.java,
                                    DATABASE_NAME
                            )
                            .addCallback(object: Callback(){
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    doAsync {
                                        PREPOPULATE_SERVICO.forEach {
                                            getDatabase(context).servicoDao().insert(it)
                                        }
                                        PREPOPULATE_CLIENTE.forEach {
                                            getDatabase(context).clienteDao().insert(it)
                                        }
                                        PREPOPULATE_VEICULO.forEach {
                                            getDatabase(context).veiculoDao().insert(it)
                                        }
                                        PREPOPULATE_ATENDIMENTO.forEach {
                                            getDatabase(context).atendimentoDao().insert(it)
                                        }
                                    }
                                }
                            })
                            .addMigrations(MIGRATION_1_2)
                            .build()
                }
            }
            return instance as AppDatabase
        }

        val PREPOPULATE_SERVICO = listOf(
                Servico(1, "Troca de pneu", 30.0, false),
                Servico(2, "Recauchutagem", 40.0, false),
                Servico(3, "Conserto de FURO simples", 15.0, false),
                Servico(4, "Conserto de FURO c/ Macarrão", 20.0, false),
        )

        val PREPOPULATE_CLIENTE = listOf(
                Cliente(
                        1, "João",
                        "joão@borracharia.com.br", "+55 (92) 987654321",
                        "01/01/1980", "001.002.003-04"
                )
        )

        val PREPOPULATE_VEICULO = listOf(
                Veiculo(1, 1, "HUR8974", "Carro"),
                Veiculo(2, 1, "TYU6630",  "Moto")
        )

        val PREPOPULATE_ATENDIMENTO = listOf(
                Atendimento(
                        1, 1,
                        "9/9/2020", 60.50
                ),
                Atendimento(
                        2, 2,
                        "19/9/2020", 100.0
                )
        )

        fun destroyInstance() {
            instance = null
        }
    }
}