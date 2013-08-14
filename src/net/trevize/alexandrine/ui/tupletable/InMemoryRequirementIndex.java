package net.trevize.alexandrine.ui.tupletable;

import java.io.IOException;

import net.trevize.alexandrine.api.Tuple;
import net.trevize.alexandrine.api.TupleList;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class InMemoryRequirementIndex {

	public static final String FIELD_ROW_INDEX = "FIELD_ROW_INDEX";

	private TupleList requirement_list;

	private RAMDirectory index;
	private WhitespaceAnalyzer analyzer;
	private IndexWriter index_writer;
	private Searcher searcher;
	private QueryParser qparser;
	private Query query;
	private TopDocs topdocs;

	public InMemoryRequirementIndex(TupleList requirement_list) {
		this.requirement_list = requirement_list;

		index = new RAMDirectory();

		try {
			analyzer = new WhitespaceAnalyzer();
			index_writer = new IndexWriter(index, analyzer, true,
					IndexWriter.MaxFieldLength.UNLIMITED);

			for (int i = 0; i < requirement_list.getRequirements().size(); ++i) {
				Tuple req = requirement_list.getRequirements().get(i);
				index_writer.addDocument(createDocument(req, i));
			}

			//optimize and close the writer to finish building the index
			index_writer.optimize();
			index_writer.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//build an IndexSearcher using the in-memory index
		try {
			searcher = new IndexSearcher(index);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Document createDocument(Tuple req, int row_index) {
		Document doc = new Document();

		doc.add(new Field(FIELD_ROW_INDEX, "" + row_index, Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		for (String field_label : req.getFieldLabels()) {
			String value = req.getFieldValue(field_label);

			if (value == null) {
				continue;
			}

			doc.add(new Field(field_label, value, Field.Store.YES,
					Field.Index.ANALYZED));
		}

		return doc;
	}

	public void search(String query_string) {
		topdocs = null;

		try {
			qparser = new MultiFieldQueryParser(Version.LUCENE_30,
					requirement_list.getRequirements().get(0).getFieldLabels()
							.toArray(new String[] {}), analyzer);
			qparser.setAllowLeadingWildcard(true);
			query = qparser.parse(query_string);
			topdocs = searcher.search(query, null, searcher.maxDoc(), new Sort(
					new SortField(FIELD_ROW_INDEX, SortField.INT)));
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void search(String field, String queryString) {
		topdocs = null;

		try {
			qparser = new QueryParser(Version.LUCENE_30, field, analyzer);
			qparser.setAllowLeadingWildcard(true);
			query = qparser.parse(queryString);
			topdocs = searcher.search(query, requirement_list.getRequirements()
					.size());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***************************************************************************
	 * getters and setters.
	 **************************************************************************/

	public TopDocs getTopdocs() {
		return topdocs;
	}

	public void setTopdocs(TopDocs topdocs) {
		this.topdocs = topdocs;
	}

	public Searcher getSearcher() {
		return searcher;
	}

	public void setSearcher(Searcher searcher) {
		this.searcher = searcher;
	}

}
