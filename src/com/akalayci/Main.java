package com.akalayci;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    static Connection con;

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/demirbas","root","");
        }catch(Exception e){ System.out.println(e);}


        Scanner scan= new Scanner(System.in,"iso-8859-9");
        int secim;

        while(true)
        {

            System.out.println("*************");
            System.out.println("1.Demirbaş Listele");
            System.out.println("2.Personel Listele");
            System.out.println("3.Personel Ekle");
            System.out.println("4.Demirbaş Ekle");
            System.out.println("5.Çıkış");
            System.out.print("Seçiminiz:");
            secim=scan.nextInt();
            System.out.println("*************");

            if(secim==1) DemirbasListele();
            if(secim==2) PersonelListele();
            if(secim==3) PersonelEkle();
            if(secim==4) DemirbasEkle();
            if(secim==5) {
                try{
                    con.close();
                }catch(Exception e){ System.out.println(e);}

                break;
            }
        }
    }

    private static void IliskiEkle(int id) {
ekraniSil();

        Scanner scan= new Scanner(System.in,"iso-8859-9");
        String yeni="";
        DemirbasListele();

        try{
            Statement stmt=con.createStatement();

            ResultSet rs=stmt.executeQuery("select * from personel Where id LIKE "+id) ;
            while(rs.next()){
            System.out.print("Ekleme İşlemi için  Demirbaş İd verin");
            int demirbas=scan.nextInt();

                yeni=  rs.getString(3)+","+demirbas;

            String sorgu=String.format("update personel set sahiplikler='%s' where id=%s ", yeni,id) ;
            int guncelleme = stmt.executeUpdate(sorgu);
            System.out.println("Kayıtlar Güncellendi");            }

        }catch(Exception e){ System.out.println();}

    }

    private static void SahiplikSil(String id,String yeni) {
        ekraniSil();

        Scanner scan= new Scanner(System.in,"iso-8859-9");
        try{


            String sorgu=String.format("update personel set sahiplikler='%s' where id=%s ", yeni,id.toString()) ;
            System.out.println(yeni);

            Statement stmt=con.createStatement();
            int guncelleme = stmt.executeUpdate(sorgu);
            System.out.println("Kayıtlar Güncellendi");
        }catch(Exception e){ System.out.println();}
    }

    private static void SahiplikSay(int id) {
        ekraniSil();

        Scanner scan= new Scanner(System.in,"iso-8859-9");

        try{
            String sahiplikler="";
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select * from personel Where id LIKE "+id) ;
            while(rs.next()){
                System.out.println(rs.getString(2)+" İsimli Personelin Demirbaşları");
                sahiplikler=rs.getString(3);

                for (int i=0;i<sahiplikler.split(",").length;i++){
                    //System.out.println(sahiplikler.split(",")[i]);
                    SahiplikGoster(sahiplikler.split(",")[i]);
                }
                System.out.print("İlişki Silmek İçin 1  Eklemek için 2 basın'     :");
                Integer girdi1 = scan.nextInt();
                if (girdi1 == 1) {
                    System.out.print("İlişki Silmek İçin Demirbaş İd Yazın Menüye Dönmek İçin '0 Basın'     :");
                } else {
                    IliskiEkle(id);

                }
                Integer girdi = scan.nextInt();
                String yaz="";

               if(girdi1==1){
                   if (girdi!=0){
                       String[] yeni =sahiplikler.split(girdi.toString());
                       for (int x =0;x<yeni.length;x++){
                           yaz=yaz+yeni[x];
                           System.out.print(yeni[x]);

                       }


                   }
                   SahiplikSil(rs.getString(1),yaz);
               }

            }



        }catch(Exception e){ System.out.println();}


    }

    private static void SahiplikGoster(String id) {
        ekraniSil();

        try{

            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select * from demirbas Where id LIKE "+id) ;
            while(rs.next())
                System.out.println("ID : "+rs.getInt(1)+"  Demirbas Adı : "+rs.getString(2)+"  Barkod : "+rs.getString(3));

        }catch(Exception e){ System.out.println();}

    }

    private static void PersonelEkle() {
        ekraniSil();

        Scanner scan= new Scanner(System.in,"iso-8859-9");
        System.out.print("Adı Soyadı     :");
        String adsoyad = scan.nextLine();
        System.out.print("Sicil No   :");

        int sicil=scan.nextInt();


        try{
            Statement stmt=con.createStatement();
            String sorgu=String.format("insert into personel(ad_soyad,sicil,sahiplikler) values('%s','%s','%s')",adsoyad, sicil, "");
            int ekleme = stmt.executeUpdate(sorgu);
            System.out.println("Kayıt Eklendi");
        }catch(Exception e){ System.out.println(e);}


    }

    public static void DemirbasListele() {
        ekraniSil();

        try{
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select * from demirbas");
            while(rs.next())
                System.out.println("ID : "+rs.getInt(1)+"  Demirbas Adı : "+rs.getString(2)+"  Barkod : "+rs.getString(3));

        }catch(Exception e){ System.out.println(e);}

    }
    public static void PersonelListele() {

        Scanner scan= new Scanner(System.in,"iso-8859-9");

ekraniSil();
        try{
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select * from personel");
            while(rs.next()) {                System.out.print("*");

                System.out.println("ID : " + rs.getInt(1) + "  Personel Ad Soyad : " + rs.getString(2) + "  Sicil: " + rs.getString(4)+"\n");



            }

            System.out.print("Demirbaş ekle-çıkar için personel id yazın //  Anamenü :0     :");
            Integer girdi = scan.nextInt();
            if (girdi!=0){
                SahiplikSay(girdi);
            }
        }catch(Exception e){ System.out.println();}

    }

    public static void DemirbasEkle() {
        ekraniSil();

        Scanner scan= new Scanner(System.in,"iso-8859-9");
        System.out.print("Demirbaş Adı     :");
        String ad=scan.next();
        System.out.print("Demirbas Barkodu :");
        String barkod=scan.next();
        try{
            Statement stmt=con.createStatement();
            String sorgu=String.format("insert into demirbas(isim,barkod,onay) values('%s','%s','%s')",ad, barkod, LocalDate.now());
            int ekleme = stmt.executeUpdate(sorgu);
            System.out.println("Kayıt Eklendi");
        }catch(Exception e){ System.out.println(e);}


    }



    private static void ekraniSil() {
        for(int i = 0; i <2; i++)

            System.out.println("\b");
    }

}