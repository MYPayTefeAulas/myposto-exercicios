package br.uea.transirie.mypay.mypaytemplate.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.*

@Dao
interface UsuarioDao:BaseDao<Usuario> {
    @Query("""SELECT * FROM $TABLE_USUARIO""")
    fun getAllUsuario():List<Usuario>

    @Query("""SELECT * FROM $TABLE_USUARIO WHERE $COLUMN_ID = :idUsuario""")
    fun usuarioById(idUsuario: Long):Usuario

    @Query("""SELECT * FROM $TABLE_USUARIO WHERE $COLUMN_EMAIL = :emailUsuario""")
    fun usuarioByEmail(emailUsuario: String):Usuario

    @Query("""SELECT 1 FROM $TABLE_USUARIO WHERE $COLUMN_PIN = :pinUsuario""")
    fun pinJaEmUso(pinUsuario: Int):Boolean

    /**
     * Retorna true caso haja um usuário com o email informado
     * @param email o email informado
     */
    @Query("SELECT 1 FROM $TABLE_USUARIO WHERE $COLUMN_EMAIL = :email")
    fun emailJaEmUso(email: String): Boolean

    /**
     *
     */
    @Query("SELECT * FROM $TABLE_USUARIO WHERE $COLUMN_PIN = :pin")
    fun usuarioByPin(pin: Int): Usuario
}