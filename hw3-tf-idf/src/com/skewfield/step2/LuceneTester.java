package com.skewfield.step2;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class LuceneTester {

    String indexDir = "D:\\UNIVERSITY\\code\\bazyabi\\lucene\\lucene\\hw3-tf-idf\\Index";
    String dataDir = "D:\\UNIVERSITY\\code\\bazyabi\\lucene\\lucene\\hw3-tf-idf\\Data";
    Indexer indexer;
    Searcher searcher;
    public  static List<String> list=new ArrayList<>();

    public static void main(String[] args) {
        LuceneTester tester;
        try {
            FileWriter fileWriter = new FileWriter(  "D:\\UNIVERSITY\\code\\bazyabi\\lucene\\lucene\\hw3-tf-idf\\saeid.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);


            tester = new LuceneTester();

            list.add("comedy franchise based around a series of films");

         //  tester.createIndex();
           // tester.search("total");
            for (int i=0;i<list.size();i++)
            tester.search(list,printWriter,i);


            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void createIndex() throws IOException {
        indexer = new Indexer(indexDir);
        int numIndexed;
        long startTime = System.currentTimeMillis();
        numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
        long endTime = System.currentTimeMillis();
        indexer.close();
        System.out.println(numIndexed+" File indexed, time taken: "
                +(endTime-startTime)+" ms");
    }

    private void search(List<String> list,PrintWriter printWriter,int i) throws IOException, ParseException {
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();
        TopDocs hits = searcher.search(list.get(i));


        long endTime = System.currentTimeMillis();

        System.out.println(hits.totalHits +
                " documents found. Time :" + (endTime - startTime));
        int j=0;
        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            j++;
            Document doc = searcher.getDocument(scoreDoc);

            //Saeid add this
            String print =list.get(i)+"  Q0  "+doc.get("path")+ "  "+j +" "+scoreDoc.score+"  SaeidAfshin1" ;
        //    System.out.println(scoreDoc.score);
        printWriter.printf(print+"\n");
       //     System.out.println("File: "
         //           + doc.get(LuceneConstants.FILE_PATH));
        }
        searcher.close();
    }
}