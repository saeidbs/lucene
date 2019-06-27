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
            FileWriter fileWriter = new FileWriter(  "D:\\UNIVERSITY\\code\\bazyabi\\lucene\\lucene\\hw3-tf-idf\\tf-idf-results.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);


            tester = new LuceneTester();

            list.add("central america");
            list.add("royal academy");
            list.add("wild west");
            list.add("fall of the berlin wall");
            list.add("equations of lower dimensionality");

         tester.createIndex();
           // tester.search("total");
            for (int i=0;i<list.size();i++)
            tester.mergeSearch(list.get(i),printWriter);
            //tester.search(list.get(i),printWriter);


            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
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

    private void search(String searchQuery,PrintWriter printWriter) throws IOException, ParseException {
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();
        TopDocs hits = searcher.search(searchQuery);


        long endTime = System.currentTimeMillis();

        System.out.println(hits.totalHits +
                " documents found. Time :" + (endTime - startTime));
        int j=0;
        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            j++;
            Document doc = searcher.getDocument(scoreDoc);

            //Saeid add this
            String split="\\";
           String[] printFile=doc.get(LuceneConstants.FILE_NAME).split("\\\\");
            printFile[printFile.length-1]=printFile[printFile.length-1].replace(".txt","");
           // System.out.println(printFile[printFile.length-1]);
          //  String print =list.get(i)+"  Q0  "+doc.get(LuceneConstants.FILE_PATH)+ "  "+j +" "+scoreDoc.score+"  SaeidAfshin1" ;
            String print =searchQuery+"  Q0  "+printFile[printFile.length-1]+ "  "+j +" "+scoreDoc.score+"  SaeidAfshin1" ;
        //    System.out.println(scoreDoc.score);
        printWriter.printf(print+"\n");
       //     System.out.println("File: "
         //           + doc.get(LuceneConstants.FILE_PATH));
        }
        searcher.close();
    }
    private void mergeSearch(String searchQuery,PrintWriter printWriter) throws Exception {
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();
        //TopDocs hits = searcher.search(searchQuery);
        List<ScoreDoc> scoreList=searcher.mergeSearch(searchQuery);

        long endTime = System.currentTimeMillis();

        System.out.println(scoreList.size() +
                " documents found. Time :" + (endTime - startTime));
        int j=0;
        for(ScoreDoc scoreDoc : scoreList) {
            j++;
            Document doc = searcher.getDocument(scoreDoc);

            //Saeid add this
            String split="\\";
           String[] printFile=doc.get(LuceneConstants.FILE_NAME).split("\\\\");
            printFile[printFile.length-1]=printFile[printFile.length-1].replace(".txt","");
           // System.out.println(printFile[printFile.length-1]);
          //  String print =list.get(i)+"  Q0  "+doc.get(LuceneConstants.FILE_PATH)+ "  "+j +" "+scoreDoc.score+"  SaeidAfshin1" ;
            String print =searchQuery+"  Q0  "+printFile[printFile.length-1]+ "  "+j +" "+scoreDoc.score+"  SaeidAfshin1" ;
        //    System.out.println(scoreDoc.score);
        printWriter.printf(print+"\n");
       //     System.out.println("File: "
         //           + doc.get(LuceneConstants.FILE_PATH));
        }
        searcher.close();
    }
}