package com.skewfield.step2;

import java.io.File;
import java.io.IOException;

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
//        phraseQuery.setSlop(1000);
//
//
//
//        return indexSearcher.search(phraseQuery, LuceneConstants.MAX_SEARCH);
        return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
    }

    public Document getDocument(ScoreDoc scoreDoc)
            throws CorruptIndexException, IOException {
        return indexSearcher.doc(scoreDoc.doc);
    }

    public void close() throws IOException {
        indexSearcher.close();
    }
}
