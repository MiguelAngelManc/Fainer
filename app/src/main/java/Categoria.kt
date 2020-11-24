import java.io.Serializable

data class Categoria (var nombre: String,
                         var semanas : ArrayList<Float>,
                      var total: Float,
                      var color: Int) : Serializable

