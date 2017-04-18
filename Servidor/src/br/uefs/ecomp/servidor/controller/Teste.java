package br.uefs.ecomp.servidor.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Teste {
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("dados\\ultimo.txt");
		PrintWriter writer = new PrintWriter(file);
		writer.print(1);
		writer.close();
		
		Scanner leitor = new Scanner(file);
		System.out.println("O inteiro é: " +leitor.nextInt());
	}
}
