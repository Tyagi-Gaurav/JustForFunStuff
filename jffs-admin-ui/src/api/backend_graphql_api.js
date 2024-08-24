import { ApolloClient, InMemoryCache, gql } from "@apollo/client";

export const apolloClient = new ApolloClient({
  uri: "/admin/graphql",
  cache: new InMemoryCache(),
});

export const getWordsGraphQL = (pageNum) => {
  return apolloClient.query({
    query: gql`
            query GetAllWords {
                allWords(pageNum: ${pageNum}) {
                    words {
                        word
                        meanings {
                            definition
                            synonyms
                            examples
                        }
                    }
                    totalWords
                    totalPages
                    currentPage
                    nextPage
                    previousPage
                }
            }`,
  });
};

export const searchGraphQL = (searchType, searchValue) => {
  return apolloClient.query({
    query: gql`
            query searchBy {
              search(searchType : ${searchType}, searchValue: "${searchValue}") {
                word
                meanings {
                    definition
                    synonyms
                    examples
                }
            }
        }`,
  });
};

export const updateWordGraphQL = (oldWord, word) => {
  var synonyms = JSON.stringify(word.meanings[0].synonyms);
  var examples = JSON.stringify(word.meanings[0].examples);
  return apolloClient.mutate({
    mutation: gql`
            mutation update {
                updateWord(oldWord: "${oldWord}", 
                           wordInput: {
                            word: "${word.word}",
                            meanings: [{
                                definition: "${word.meanings[0].definition}",
                                synonyms: ${synonyms},
                                examples: ${examples}
                            }]
                })
            }`,
  });
};

export const deleteWordGraphQL = (word) => {
  return apolloClient.mutate({
    mutation: gql`
            mutation delete {
                deleteWord(word: "${word}")
            }`,
  });
};

export const addWordGraphQL = (wordInput) => {
  var synonyms = JSON.stringify(wordInput.meanings[0].synonyms);
  var examples = JSON.stringify(wordInput.meanings[0].examples);
  var request = {
    mutation: gql`
            mutation add {
                addWord(wordInput: {
                word: "${wordInput.word}",
                meanings: [{
                    definition: "${wordInput.meanings[0].definition}",
                    synonyms: ${synonyms},
                    examples: ${examples}
                }]
            })}`,
  };
  return apolloClient.mutate(request);
};