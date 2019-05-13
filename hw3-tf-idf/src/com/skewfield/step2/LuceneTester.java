package com.skewfield.step2;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class LuceneTester {

    String indexDir = "D:\\UNIVERSITY\\code\\bazyabi\\lucene\\lucene\\hw3-tf-idf\\Index";
    String dataDir = "D:\\UNIVERSITY\\code\\bazyabi\\lucene\\lucene\\hw3-tf-idf\\Data";
    Indexer indexer;
    Searcher searcher;

    public static void main(String[] args) {
        LuceneTester tester;
        try {
            FileWriter fileWriter = new FileWriter(  "D:\\UNIVERSITY\\code\\bazyabi\\lucene\\lucene\\hw3-tf-idf\\saeid.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);


            tester = new LuceneTester();
         //  tester.createIndex();
           // tester.search("total");
            tester.search("comedy franchise based around a series of films",printWriter);


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

    private void search(String searchQuery,PrintWriter printWriter) throws IOException, ParseException {
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();
        TopDocs hits = searcher.search(searchQuery);


        long endTime = System.currentTimeMillis();

        System.out.println(hits.totalHits +
                " documents found. Time :" + (endTime - startTime));
        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.getDocument(scoreDoc);

            //Saeid add this
            System.out.println(scoreDoc.score);
        printWriter.printf(scoreDoc.score+"\n");
            System.out.println("File: "
                    + doc.get(LuceneConstants.FILE_PATH));
        }
        searcher.close();
    }
}