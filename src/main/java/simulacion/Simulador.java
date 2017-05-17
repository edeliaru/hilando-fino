package simulacion;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

public class Simulador {

    private double eficiencia_Acetex;
    private double eficiencia_Vardham;
    private double eficiencia_GPI;
    private double eficiencia_Sportking;
    private double eficiencia_Windsom;
    private double eficiencia_TDB;

    private double kg_JerseyLycra;
    private double kg_Frisa;

    //VARIABLES DE CONTROL : KG ENTREGADOS POR PROVEEDORES
    private double kg_Acetex=300;
    private double kg_Vardham=300;
    private double kg_GPI=2200;
    private double kg_Sportking=2330;
    private double kg_Windsom=2200;
    private double kg_TDB=2500;

    //VARIABLES DE ESTADO : CANTIDAD DE STOCK DE LOS hilos
    private double st_Acetex=250;
    private double st_Vardham=50;
    private double st_GPI=0;
    private double st_Sportking=100;
    private double st_Windsom=1000;
    private double st_TDB=1700;

    //VALOR FIJO PRECIO HILO
//    private double costo_Acetex=50;
//    private double costo_Vardham=52;
//    private double costo_GPI=47;
//    private double costo_Sportking=45;
//    private double costo_Windsom=38;
//    private double costo_TDB=40;

    double cant_teoirica_a =0;
    double cant_real_a =0;
    double cant_teoirica_v =0;
    double cant_real_v =0;
    double cant_teoirica_s =0;
    double cant_real_s =0;
    double cant_teoirica_g =0;
    double cant_real_g =0;
    double cant_teoirica_t =0;
    double cant_real_t =0;
    double cant_teoirica_w =0;
    double cant_real_w =0;

    public void simular(){
        double kgRealesJL=0;
        double kgRealesFrisa=0;
        int time=0;
        int tiempoFinal=10000;

        while(time<=tiempoFinal){
            time++;
            kg_JerseyLycra=500;
            kg_Frisa=1200;
            ingresaStock(time);
            kgRealesJL += fabricarJL();
            kgRealesFrisa += fabricarFrisa();

            System.out.println("_____DIA"+time+"_______");
            printDouble("a",st_Acetex);
            printDouble("v",st_Vardham);
            printDouble("gpi",st_GPI);
            printDouble("sp",st_Sportking);
            printDouble("wi",st_Windsom);
            printDouble("tdb",st_TDB);

        }

        calcularResultados(kgRealesJL,kgRealesFrisa,time);

    }

    private void ingresaStock(int time){
        switch(this.getMod(time)){
            case 1:
                this.llegaSportking();
                break;
            case 2:
                this.llegaAcetex();
                break;
            case 3:
                this.llegaGPI();
                break;
            case 4:
                this.llegaWindsom();
                this.llegaVardham();
                break;
            case 5:
                this.llegaTDB();
            default:
                break;
        }
    }

    public int getMod(int time){
        return (time % 6);
    }
    private void llegaTDB() {
        st_TDB+=kg_TDB;
    }
    private void llegaVardham() {
        st_Vardham+=kg_Vardham;
    }
    private void llegaGPI() {
        st_GPI+=kg_GPI;
    }
    private void llegaWindsom() {
        st_Windsom+=kg_Windsom;
    }
    private void llegaAcetex() {
        st_Acetex+=kg_Acetex;
    }
    private void llegaSportking() {
        st_Sportking+=kg_Sportking;
    }

    public double fabricarJL(){

        double algpes241 = 0.2 * kg_JerseyLycra;
        double algpol121 = 0.8 * kg_JerseyLycra;

        double kgRealesAlgpes241 = utilizarHiloAlgpes241(algpes241);
        double kgRealesAlgpes121 = utilizarHiloAlgpol121(algpol121);

        return kgRealesAlgpes121+kgRealesAlgpes241;
    }
    public double fabricarFrisa(){
        double algpes121 = 0.7 * kg_Frisa;
        double algpol121 = 0.3 * kg_Frisa;

        double kgRealesAlgpes121 =  utilizarHiloAlgpes121(algpes121);
        double kgRealesAlgpol121 = utilizarHiloAlgpol121(algpol121);

        return kgRealesAlgpes121+kgRealesAlgpol121;
    }

    public double utilizarHiloAlgpes241(double kg_hilo) {
        double cant_prod_p1=0;
        double cant_prod_p2=0;

            if (st_Acetex>= st_Vardham) {
                eficiencia_Acetex=getEfiAcetex();
                cant_teoirica_a+=Math.min(st_Acetex-st_Vardham,kg_hilo);
                cant_real_a+=Math.min(st_Acetex-st_Vardham,kg_hilo)*eficiencia_Acetex;
                cant_prod_p1=Math.min(st_Acetex-st_Vardham,kg_hilo)*eficiencia_Acetex;
                st_Acetex -= cant_prod_p1;
                kg_hilo -= cant_prod_p1;

                eficiencia_Vardham= getEfiVardham();
                cant_teoirica_v+=Math.min(st_Vardham,kg_hilo);
                cant_real_v+=Math.min(st_Vardham,kg_hilo)*eficiencia_Vardham;
                cant_prod_p2=Math.min(st_Vardham,kg_hilo)*eficiencia_Vardham;
                st_Vardham -= cant_prod_p2;
                kg_hilo -= cant_prod_p2;

            } else {
                eficiencia_Vardham= getEfiVardham();
                cant_teoirica_v+=Math.min(st_Vardham-st_Acetex,kg_hilo);
                cant_real_v+=Math.min(st_Vardham-st_Acetex,kg_hilo)*eficiencia_Vardham;
                cant_prod_p2=Math.min(st_Vardham-st_Acetex,kg_hilo)*eficiencia_Vardham;
                st_Vardham -= cant_prod_p2;
                kg_hilo -= cant_prod_p2;

                eficiencia_Acetex=getEfiAcetex();
                cant_teoirica_a+=Math.min(st_Acetex,kg_hilo);
                cant_real_a += Math.min(st_Acetex,kg_hilo)*eficiencia_Acetex;
                cant_prod_p1 = Math.min(st_Acetex,kg_hilo)*eficiencia_Acetex;
                st_Acetex -= cant_prod_p1;
                kg_hilo -= cant_prod_p2;

        }
        return cant_prod_p2 + cant_prod_p1;
    }
    public double utilizarHiloAlgpes121(double kg_hilo) {
        double cant_prod_p1;
        double cant_prod_p2;

            if (st_Windsom>= st_TDB) {
                eficiencia_Windsom=getEfiWindsom();
                cant_teoirica_w+=Math.min(st_Windsom-st_TDB,kg_hilo);
                cant_real_w+=Math.min(st_Windsom-st_TDB,kg_hilo)*eficiencia_Windsom;
                cant_prod_p1=Math.min(st_Windsom-st_TDB,kg_hilo)*eficiencia_Windsom;
                st_Windsom -= cant_prod_p1;
                kg_hilo -= cant_prod_p1;

                eficiencia_TDB=gerEfiTDB();
                cant_teoirica_t+=Math.min(st_TDB,kg_hilo);
                cant_real_t+=Math.min(st_TDB,kg_hilo)*eficiencia_TDB;
                cant_prod_p2=Math.min(st_TDB,kg_hilo)*eficiencia_TDB;
                st_TDB -= cant_prod_p2;
                kg_hilo -= cant_prod_p2;

            } else {
                eficiencia_TDB=gerEfiTDB();
                cant_teoirica_t+=Math.min(st_TDB-st_Windsom,kg_hilo);
                cant_real_t+=Math.min(st_TDB-st_Windsom,kg_hilo)*eficiencia_TDB;
                cant_prod_p2=Math.min(st_TDB-st_Windsom,kg_hilo)*eficiencia_TDB;
                st_TDB -= cant_prod_p2;
                kg_hilo -= cant_prod_p2;

                eficiencia_Windsom=getEfiWindsom();
                cant_teoirica_w+=Math.min(st_Windsom,kg_hilo);
                cant_real_w+=Math.min(st_Windsom,kg_hilo)*eficiencia_Windsom;
                cant_prod_p1=Math.min(st_Windsom,kg_hilo)*eficiencia_Windsom;
                st_Windsom -= cant_prod_p1;
                kg_hilo -= cant_prod_p1;
            }

        return cant_prod_p2 + cant_prod_p1;

    }
    public double utilizarHiloAlgpol121(double kg_hilo) {
        double cant_prod_p1;
        double cant_prod_p2;

            if (st_Sportking>= st_GPI) {
                eficiencia_Sportking=getEfiSportking();
                cant_teoirica_s+=Math.min(st_Sportking-st_GPI,kg_hilo);
                cant_real_s+=Math.min(st_Sportking-st_GPI,kg_hilo*eficiencia_Sportking);
                cant_prod_p1=Math.min(st_Sportking-st_GPI,kg_hilo*eficiencia_Sportking);
                st_Sportking -= cant_prod_p1;
                kg_hilo -= cant_prod_p1;

                eficiencia_GPI=getEfiGPI();
                cant_teoirica_g+=Math.min(st_GPI,kg_hilo);
                cant_real_g+=Math.min(st_GPI,kg_hilo*eficiencia_GPI);
                cant_prod_p2=Math.min(st_GPI,kg_hilo*eficiencia_GPI);
                st_GPI -= cant_prod_p2;
                kg_hilo -= cant_prod_p2;

            } else {
                eficiencia_GPI=getEfiGPI();
                cant_teoirica_s+=Math.min(st_GPI-st_Sportking,kg_hilo);
                cant_real_s+=Math.min(st_GPI-st_Sportking,kg_hilo*eficiencia_GPI);
                cant_prod_p2=Math.min(st_GPI-st_Sportking,kg_hilo*eficiencia_GPI);
                st_GPI -= cant_prod_p2;
                kg_hilo -= cant_prod_p2;

                eficiencia_Sportking=getEfiSportking();
                cant_teoirica_g+=Math.min(st_Sportking,kg_hilo);
                cant_real_g+=Math.min(st_Sportking,kg_hilo*eficiencia_Sportking);
                cant_prod_p1 = Math.min(st_Sportking,kg_hilo*eficiencia_Sportking);
                st_Sportking -= cant_prod_p1;
                kg_hilo -= cant_prod_p1;
            }
        return cant_prod_p2 + cant_prod_p1;


    }
    public void calcularResultados(double kgRealesJL, double kgRealesFrisa, double time){
        double promKgRealesJL = kgRealesJL/time;
        double promkgRealesFrisa = kgRealesFrisa/time;

        double inutilidad_a = (cant_teoirica_a-cant_real_a);
        double inutilidad_v = (cant_teoirica_v-cant_real_v);
        double inutilidad_s = (cant_teoirica_s-cant_real_s);
        double inutilidad_g = (cant_teoirica_g-cant_real_g);
        double inutilidad_t = (cant_teoirica_t-cant_real_t);
        double inutilidad_w = (cant_teoirica_w-cant_real_w);

        System.out.println("---------------Resultados Obtenidos------------------------");
        System.out.println("-----------------------------------------------------------");

        System.out.println("-----Para fabricar: "+kg_JerseyLycra+"kg de JerseyLycara ");
        System.out.println("Variables de control: "+ " \n Kg_Acetex:"+kg_Acetex+""+ " \n Kg_Vardham:"+kg_Vardham+"\n Kg_Sportking:"+kg_Sportking+"\n Kg_GPI:"+kg_GPI);
        printDouble("El promedio KgRealesJL por día",promKgRealesJL);
        System.out.println("");
        System.out.println("-----Para fabricar "+kg_Frisa+"kg de Frisa ");
        System.out.println("Variables de control: "+" \n Kg_Windsom:"+kg_Windsom+""+ " \n Kg_TDB:"+kg_TDB+"\n Kg_Sportking:"+kg_Sportking+"\n Kg_GPI:"+kg_GPI);
        printDouble("El promedio KgRealesFrisa por día",promkgRealesFrisa);
        System.out.println("");

        printDouble("-----Cantidad promedio mensual de hilo Acetex inutilizado",inutilidad_a/time*30);
        printDouble("-----Cantidad promedio mensual de hilo Vardham inutilizado",inutilidad_v/time*30);
        printDouble("-----Cantidad promedio mensual de hilo GPI inutilizado",inutilidad_g/time*30);
        printDouble("-----Cantidad promedio mensual de hilo Sportking inutilizado",inutilidad_s/time*30);
        printDouble("-----Cantidad promedio mensual de hilo Windsom inutilizado",inutilidad_w/time *30);
        printDouble("-----Cantidad promedio mensual de hilo TDB inutilizado",inutilidad_t/time*30);
    }

    public double getEfiSportking() {
        double neg = (-1)* getRandom();
        return -0.11103*(neg-6.56606);
    }
    public double getEfiVardham() {
        double r1 = getRandom();
        double r2 = getRandom();
        double xi = 0.8+0.1*r1;
        double yi = 10.87*r2;

        double exp1= Math.pow((xi/0.88164), 25.031);
        double e=2.71828182846;
        double exp2= Math.pow((xi/0.88164),26.031);
        double exp3= Math.pow(e, -exp2);

        double expre=(26.031/0.88164)* exp1 * exp3;

        while(yi>expre){
            r1 = getRandom();
            r2 = getRandom();
            xi = 0.8+0.10*r1;
            yi = 10.87*r2;

            exp1= Math.pow((xi/0.88164), 25.031);
            e=2.71828182846;
            exp2= Math.pow((xi/0.88164),26.031);
            exp3= Math.pow(e, -exp2);
            expre=(26.031/0.88164)* exp1 * exp3;
        }

        return xi;
    }
    public double getEfiAcetex() {
        double neg = (-1)* getRandom();
        return -0.1*(neg-7.5);
    }
    public double getEfiGPI() {
        return (-0.2496)*(-getRandom()-2.29425);
    }
    public double getEfiWindsom() {
        double exp= Math.pow(1-getRandom(), 0.74324);
        return -0.174735*(exp-4.97081);
    }
    public double gerEfiTDB() {
        double r=getRandom();
        return 0.84931-0.01603 * Math.tan(1.5708-3.14159*r);
    }

    public double getRandom() {
        Random generator = new Random();
        double number = generator.nextDouble();
        return number;
    }
    public void printDouble(String name,double d){
        String value =new DecimalFormat("#.##").format(d);
        System.out.println(name+": "+value+",");
    }
}
