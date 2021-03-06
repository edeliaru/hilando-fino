package simulacion;
import java.text.DecimalFormat;
import java.util.Random;

public class Simulador {

    //VARIABLES DE CONTROL
    private double kg_Acetex=500;
    private double kg_Vardham=500;
    private double kg_Sportking=2000;
    private double kg_GPI=2000;
    private double kg_Windsom=1000;
    private double kg_TDB=1000;

    //VALOR FIJO PRECIO HILO
    private double costo_Acetex=50;
    private double costo_Vardham=53;
    private double costo_GPI=48;
    private double costo_Sportking=45;
    private double costo_Windsom=38;
    private double costo_TDB=41;

    //VARIABLES DE ESTADO
    private double st_Acetex=500;
    private double st_Vardham=500;
    private double st_Sportking=2000;
    private double st_GPI=2000;
    private double st_Windsom=1000;
    private double st_TDB=1000;

    int time=0;
    int tiempoFinal=1000000;

    double cant_prod_a =0;
    double cant_prod_v =0;
    double cant_prod_s =0;
    double cant_prod_g =0;
    double cant_prod_t =0;
    double cant_prod_w =0;

    double costos_tot_jl=0;
    double costos_tot_f=0;

    private double eficiencia_Acetex;
    private double eficiencia_Vardham;
    private double eficiencia_GPI;
    private double eficiencia_Sportking;
    private double eficiencia_Windsom;
    private double eficiencia_TDB;

    private double kg_JerseyLycra=500;
    private double kg_Frisa=1200;

    public void simular(){
        double kgRealesJL=0;
        double kgRealesFrisa=0;
        while(time<=tiempoFinal){
            time++;
            ingresaStock(time);
            kgRealesJL += fabricarJL();
            costos_tot_jl += cant_prod_a*costo_Acetex+cant_prod_v*costo_Vardham+cant_prod_g*costo_GPI+cant_prod_s*costo_Sportking;
            kgRealesFrisa += fabricarFrisa();
            costos_tot_f += cant_prod_w*costo_Windsom+cant_prod_t*costo_TDB+cant_prod_g*costo_GPI+cant_prod_s*costo_Sportking;
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

        double lycra = 0.2 * kg_JerseyLycra;
        double algpol121 = 0.8 * kg_JerseyLycra;

        double kgRealesLycra = utilizarHiloLycra(lycra);
        double kgRealesAlgpol121 = utilizarHiloAlgpol121(algpol121);
        double total = kgRealesAlgpol121+kgRealesLycra;
        return total;
    }
    public double fabricarFrisa(){
        double algpes121 = 0.7 * kg_Frisa;
        double algpol121 = 0.3 * kg_Frisa;

        double kgRealesAlgpes121 =  utilizarHiloAlgpes121(algpes121);
        double kgRealesAlgpol121 = utilizarHiloAlgpol121(algpol121);

        return kgRealesAlgpes121+kgRealesAlgpol121;
    }

    public double utilizarHiloLycra(double kg_hilo) {
        double cant_prod_p1=0;
        double cant_prod_p2=0;
        double cant_teorica;

            if (st_Acetex>= st_Vardham) {
                eficiencia_Acetex=getEfiAcetex();
                cant_teorica=Math.min(st_Acetex-st_Vardham,kg_hilo);
                cant_prod_p1=Math.min(st_Acetex-st_Vardham,kg_hilo)*eficiencia_Acetex;
                st_Acetex -= cant_prod_p1;
                kg_hilo -= cant_teorica;
                if(kg_hilo>0){
                    eficiencia_Vardham= getEfiVardham();
                    cant_prod_p2=Math.min(st_Vardham,kg_hilo)*eficiencia_Vardham;
                    st_Vardham -= cant_prod_p2;
                }
            } else {
                eficiencia_Vardham= getEfiVardham();
                cant_teorica=Math.min(st_Vardham-st_Acetex,kg_hilo);
                cant_prod_p2=Math.min(st_Vardham-st_Acetex,kg_hilo)*eficiencia_Vardham;
                st_Vardham -= cant_prod_p2;
                kg_hilo -= cant_teorica;
                if(kg_hilo>0){
                    eficiencia_Acetex=getEfiAcetex();
                    cant_prod_p1 = Math.min(st_Acetex,kg_hilo)*eficiencia_Acetex;
                    st_Acetex -= cant_prod_p1;
                }
        }
        cant_prod_a=cant_prod_p1;
        cant_prod_v=cant_prod_p2;
        return cant_prod_p2 + cant_prod_p1;
    }
    public double utilizarHiloAlgpes121(double kg_hilo) {
        double cant_prod_p1=0;
        double cant_prod_p2=0;
        double cant_teorica;
            if (st_Windsom>= st_TDB) {
                eficiencia_Windsom=getEfiWindsom();
                cant_teorica=Math.min(st_Windsom-st_TDB,kg_hilo);
                cant_prod_p1=Math.min(st_Windsom-st_TDB,kg_hilo)*eficiencia_Windsom;
                st_Windsom -= cant_prod_p1;
                kg_hilo -= cant_teorica;
                if(kg_hilo>0){
                    eficiencia_TDB=getEfiTDB();
                    cant_prod_p2=Math.min(st_TDB,kg_hilo)*eficiencia_TDB;
                    st_TDB -= cant_prod_p2;
                }
            } else {
                eficiencia_TDB=getEfiTDB();
                cant_teorica=Math.min(st_TDB-st_Windsom,kg_hilo);
                cant_prod_p2=Math.min(st_TDB-st_Windsom,kg_hilo)*eficiencia_TDB;
                st_TDB -= cant_prod_p2;
                kg_hilo -= cant_teorica;

                if (kg_hilo>0){
                    eficiencia_Windsom=getEfiWindsom();
                    cant_prod_p1=Math.min(st_Windsom,kg_hilo)*eficiencia_Windsom;
                    st_Windsom -= cant_prod_p1;
                }
            }
        cant_prod_w=cant_prod_p1;
        cant_prod_t=cant_prod_p2;
        return cant_prod_p2 + cant_prod_p1;
    }

    public double utilizarHiloAlgpol121(double kg_hilo) {
        double cant_prod_p1=0;
        double cant_prod_p2=0;
        double cant_teorica;

            if (st_Sportking>= st_GPI) {
                eficiencia_Sportking=getEfiSportking();
                cant_teorica=Math.min(st_Sportking-st_GPI,kg_hilo);
                cant_prod_p1=Math.min(st_Sportking-st_GPI,kg_hilo)*eficiencia_Sportking;
                st_Sportking -= cant_prod_p1;
                kg_hilo -= cant_teorica;
                if (kg_hilo>0){
                    eficiencia_GPI=getEfiGPI();
                    cant_prod_p2=Math.min(st_GPI,kg_hilo)*eficiencia_GPI;
                    st_GPI -= cant_prod_p2;
                }
            } else {
                eficiencia_GPI=getEfiGPI();
                cant_teorica=Math.min(st_GPI-st_Sportking,kg_hilo);
                cant_prod_p2=Math.min(st_GPI-st_Sportking,kg_hilo)*eficiencia_GPI;
                st_GPI -= cant_prod_p2;
                kg_hilo -= cant_teorica;

                if (kg_hilo>0){
                    eficiencia_Sportking=getEfiSportking();
                    cant_prod_p1= Math.min(st_Sportking,kg_hilo)*eficiencia_Sportking;
                    st_Sportking -= cant_prod_p1;
                }
            }
        cant_prod_s=cant_prod_p1;
        cant_prod_g=cant_prod_p2;
        return cant_prod_p2 + cant_prod_p1;
    }

    public void calcularResultados(double kgRealesJL, double kgRealesFrisa, double time){
        double promKgRealesJL = kgRealesJL/time;
        double promkgRealesFrisa = kgRealesFrisa/time;

        System.out.println("---------------Resultados Obtenidos------------------------");
        System.out.println("-----------------------------------------------------------");

        System.out.println("-----Para fabricar: "+kg_JerseyLycra+"kg de JerseyLycara ");
        System.out.println("Variables de control: "+ " \n Kg_Acetex:"+kg_Acetex+""+ " \n Kg_Vardham:"+kg_Vardham+"\n Kg_Sportking:"+kg_Sportking+"\n Kg_GPI:"+kg_GPI);
        printDouble("Prom_Kg_Prod_JerseyLycra_xdia",promKgRealesJL);
        printDouble("Prom_Costo_Kg_JerseyLycra",(costos_tot_jl/time)/promKgRealesJL);

        System.out.println("");
        System.out.println("-----Para fabricar "+kg_Frisa+"kg de Frisa ");
        System.out.println("Variables de control: "+" \n Kg_Windsom:"+kg_Windsom+""+ " \n Kg_TDB:"+kg_TDB+"\n Kg_Sportking:"+kg_Sportking+"\n Kg_GPI:"+kg_GPI);
        printDouble("Kg_Prod_Frisa_xdia",promkgRealesFrisa);
        printDouble("Prom_Costo_Kg_Frisa",(costos_tot_f/time)/promkgRealesFrisa);
        System.out.println("");
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
    public double getEfiTDB() {
        double r=getRandom();
        return 0.84931-0.01603 * Math.tan(1.5708-3.14159*r);
    }

    public double getRandom() {
        Random generator = new Random();
        double number = generator.nextDouble();
        return number;
    }
    public void printDouble(String s,double d){
        String value =new DecimalFormat("#.##").format(d);
        System.out.println(s+" "+value+".");
    }
    public String printDouble(double d){
       String value =new DecimalFormat("#.##").format(d);
       return value;
    }
}

