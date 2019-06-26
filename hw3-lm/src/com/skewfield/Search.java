package com.skewfield;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class Search {

    public static TopDocs searchInContent(String textToFind, IndexSearcher searcher) throws Exception {
        //Create search query
        QueryParser qp = new QueryParser("contents", new StandardAnalyzer());
        Query query = qp.parse(textToFind);

        //search the index
//        String[] strings=textToFind.split(" ");
//        for (int i=0;i<strings.length;i++){
//            System.out.println(strings[i]);
//        }
     PhraseQuery phraseQuery = new PhraseQuery(1000,"contents",textToFind.split(" "));
       TopDocs hits = searcher.search(phraseQuery, 10);

//        PhraseQuery.Builder builder = new PhraseQuery.Builder();
//        builder.add(new Term("contents", "central"), 0);
//        builder.add(new Term("contents", "america"), 1);
//        builder.setSlop(1);
//        PhraseQuery pq = builder.build();

       // TopDocs hits = searcher.search(pq, 10);

      // TopDocs hits = searcher.search(query, 10);
       // Explanation explanation=searcher.explain(query,4);

        return hits;
    }

    public static IndexSearcher createSearcher(String indexPath) throws IOException {
        Directory dir = FSDirectory.open(Paths.get(indexPath));

        //It is an interface for accessing a point-in-time view of a lucene index
        IndexReader reader = DirectoryReader.open(dir);

        //Index searcher
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }
}
