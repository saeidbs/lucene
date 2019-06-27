package com.skewfield.step2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Searcher {

    IndexSearcher indexSearcher;
    QueryParser queryParser;
    Query query;

    public Searcher(String indexDirectoryPath)
            throws IOException {
        Directory indexDirectory =
                FSDirectory.open(new File(indexDirectoryPath));
        indexSearcher = new IndexSearcher(indexDirectory);
        queryParser = new QueryParser(Version.LUCENE_36,
                LuceneConstants.CONTENTS,
                new StandardAnalyzer(Version.LUCENE_36));
    }

    public TopDocs search( String searchQuery)
            throws IOException, ParseException {
        query = queryParser.parse(searchQuery);

//        PhraseQuery phraseQuery = new PhraseQuery();
//
//        String[] strings=searchQuery.split(" ");
//        for (int i=0;i<strings.length;i++) {
//            phraseQuery.add(new Term(LuceneConstants.CONTENTS, strings[i]), i);
//
//        }
//        phraseQuery.setSlop(0);
//
//        TopDocs phraseQueryTopDocs=indexSearcher.search(phraseQuery, LuceneConstants.MAX_SEARCH);
//
//
//        return phraseQueryTopDocs;
       return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
    }
    public  List<ScoreDoc> mergeSearch(String textToFind, IndexSearcher searcher) throws Exception {
        List<ScoreDoc> list=new ArrayList<>();

        //Create search query
        queryParser = new QueryParser(Version.LUCENE_36,
                LuceneConstants.CONTENTS,
                new StandardAnalyzer(Version.LUCENE_36));
        Query query = queryParser.parse(textToFind);

        TopDocs standardHits = searcher.search(query, 1000);

        ScoreDoc[] standardScoreDoce =standardHits.scoreDocs;
        //search the index
//        String[] strings=textToFind.split(" ");
//        for (int i=0;i<strings.length;i++){
//            System.out.println(strings[i]);
//        }
        PhraseQuery phraseQuery = new PhraseQuery();

        String[] strings=textToFind.split(" ");
        for (int i=0;i<strings.length;i++) {
           phraseQuery.add(new Term(LuceneConstants.CONTENTS, strings[i]), i);
      }
     phraseQuery.setSlop(1000);
        TopDocs phraseHits = searcher.search(phraseQuery, 1000);
        ScoreDoc[] phraseScoreDocs =phraseHits.scoreDocs;


        Document standardDoc;
        Document phraseDoc;
        //  System.out.println(standardScoreDoce.length);
        boolean bool=false;
        for(int i=0;i<standardScoreDoce.length;i++){
            standardDoc= searcher.doc(standardScoreDoce[i].doc);
            ScoreDoc mergeDoc;

            //   System.out.println(phraseScoreDocs.length);
            for (int j=0;j<phraseScoreDocs.length;j++){
                phraseDoc= searcher.doc(phraseScoreDocs[j].doc);
                if(standardDoc.get("path").equals(phraseDoc.get("path"))){
                    mergeDoc=standardScoreDoce[i];
                    mergeDoc.score=(mergeDoc.score+phraseScoreDocs[j].score)/2;
                    list.add(mergeDoc);
                    bool=true;
                    break;
                }

            }
            if(!bool) {
                mergeDoc = standardScoreDoce[i];
                list.add(mergeDoc);
            }
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




    public Document getDocument(ScoreDoc scoreDoc)
            throws CorruptIndexException, IOException {
        return indexSearcher.doc(scoreDoc.doc);
    }

    public void close() throws IOException {
        indexSearcher.close();
    }
}
