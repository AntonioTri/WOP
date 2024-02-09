package Progetto_prog_3.objects.Prototype;

//Classe che possiede un solo metodo, questo prende in input una classe che implementa
//L'interfaccia Projectile Interface e ne restituisce un clone utilizzando il metodo internamente implementato
public class Cloningfactory {

    public ProjectileInterface getClone(ProjectileInterface projectileSample){

        return projectileSample.makeClone();

    }
}
