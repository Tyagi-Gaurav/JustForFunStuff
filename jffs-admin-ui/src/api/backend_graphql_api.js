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
    return apolloClient.mutate({
        mutation: gql`
                mutation update {
                  updateWord(oldWord: "${oldWord}", wordInput: ${word})
            }`
      });
  };

  export const deleteWordGraphQL = (word) => {
    return apolloClient.mutate({
        mutation: gql`
                mutation delete {
                    deleteWord(word: "${word}")
                }`
      });
  };
  