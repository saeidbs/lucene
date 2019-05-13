package com.skewfield;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;


import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;

public class Main {
    private static final String indexPath = "D:\\UNIVERSITY\\code\\bazyabi\\lucene\\lucene\\hw3-lm\\Index";


    private static final    String docsPath = "D:\\UNIVERSITY\\code\\bazyabi\\lucene\\lucene\\hw3-tf-idf\\Data";
    public static void main(String[] args) throws IOException, Exception {


        //Input Path Variable
        final Path docDir = Paths.get(docsPath);

        try {
            //org.apache.lucene.store.Directory instance
            Directory dir = FSDirectory.open(Paths.get(indexPath));

            //analyzer with the default stop words
            StandardAnalyzer analyzer = new StandardAnalyzer();

            //IndexWriter Configuration
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
            LMJelinekMercerSimilarity sim = new LMJelinekMercerSimilarity(0.009f);
            iwc.setSimilarity(sim);
            //IndexWriter writes new index files to the directory
            IndexWriter writer = new IndexWriter(dir, iwc);

            //Its recursive method to iterate all files and directories
            Index.indexDocs(writer, docDir);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Create lucene searcher. It search over a single IndexReader.
        IndexSearcher searcher = Search.createSearcher(indexPath);


        //Search indexed contents using search term


        List<String> list =new ArrayList<>();
        list.add("comedy franchise based around a series of films");


        FileWriter fileWriter = new FileWriter( "D:\\UNIVERSITY\\code\\bazyabi\\lucene\\lucene\\hw3-lm\\saeid.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for (int i = 0; i < 1; i++) {



//            printWriter.printf("Blog name is %s", "howtodoinjava.com\n");
//            printWriter.printf("wow");



            TopDocs foundDocs = Search.searchInContent(list.get(i), searcher);


            //Total found documents
            System.out.println("Total Results :: " + foundDocs.totalHits);

            //Let's print out the path of files which have searched term
            int j=1;
            for (ScoreDoc sd : foundDocs.scoreDocs) {
                j++;
                Document d = searcher.doc(sd.doc);
              //  System.out.println("Path : " + d.get("path") + ", Score : " + sd.score);


                String print =" query_id  \\t Q0 \\t document_id \\t "+j+" \\t your_login" ;
                printWriter.printf("Path : " + d.get("path") + ", Score : " + sd.score +"\n");


            }


        }

        printWriter.close();

    }


}


