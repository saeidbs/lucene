package com.skewfield;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

        // Explanation explanation=searcher.explain(query,4);
        //TopDocs hits = searcher.search(query, 10);


        return hits;
    }

    public static List<ScoreDoc> mergeSearch(String textToFind, IndexSearcher searcher) throws Exception {
       List<ScoreDoc> list=new ArrayList<>();

        //Create search query
        QueryParser qp = new QueryParser("contents", new StandardAnalyzer());
        Query query = qp.parse(textToFind);

        TopDocs standardHits = searcher.search(query, 1000);

        ScoreDoc[] standardScoreDoce =standardHits.scoreDocs;
        //search the index
//        String[] strings=textToFind.split(" ");
//        for (int i=0;i<strings.length;i++){
//            System.out.println(strings[i]);
//        }
        PhraseQuery phraseQuery = new PhraseQuery(100,"contents",textToFind.split(" "));
        TopDocs phraseHits = searcher.search(phraseQuery, 1000);
        ScoreDoc[] phraseScoreDocs =phraseHits.scoreDocs;


        Document standardDoc;
        Document phraseDoc;
      //  System.out.println(standardScoreDoce.length);
        boolean bool=false;
        for(int i=0;i<standardScoreDoce.length;i++){
            standardDoc= searcher.doc(standardScoreDoce[i].doc);
            ScoreDoc mergeDoc  =standardScoreDoce[i];;

         //   System.out.println(phraseScoreDocs.length);

            for (int j=0;j<phraseScoreDocs.length;j++){
               phraseDoc= searcher.doc(phraseScoreDocs[j].doc);
                if(standardDoc.get("path").equals(phraseDoc.get("path"))){
                    mergeDoc.score=(mergeDoc.score+phraseScoreDocs[j].score)/2;
                    bool=true;
                    break;
                }

            }
            if(!bool) {
                mergeDoc.score=mergeDoc.score/2;
            }
                list.add(mergeDoc);
            bool=false;

        }

        list.sort(new Comparator<ScoreDoc>() {
            @Override
            public int compare(ScoreDoc o1, ScoreDoc o2) {
                if(o1.score>o2.score) {
                    return -1;
                }else if(o1.score<o2.score){
                    return 1;
                }
                return 0;
            }
        });


        List<ScoreDoc>finalList=new ArrayList<>();

        for(int i=0;i<10;i++){
            if(i<list.size()){
                finalList.add(list.get(i));

            }
        }




//        PhraseQuery.Builder builder = new PhraseQuery.Builder();
//        builder.add(new Term("contents", "central"), 0);
//        builder.add(new Term("contents", "america"), 1);
//        builder.setSlop(1);
//        PhraseQuery pq = builder.build();

        // TopDocs hits = searcher.search(pq, 10);

        // Explanation explanation=searcher.explain(query,4);

        //System.out.println("done");
        return finalList;
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
