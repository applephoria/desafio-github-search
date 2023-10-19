package br.com.igorbag.githubsearch.ui

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.com.igorbag.githubsearch.R
import br.com.igorbag.githubsearch.data.GitHubService
import br.com.igorbag.githubsearch.domain.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import br.com.igorbag.githubsearch.ui.adapter.RepositoryAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var nomeUsuario: EditText
    private lateinit var btnConfirmar: Button
    private lateinit var listaRepositories: RecyclerView
    private lateinit var githubApi: GitHubService
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var repositoryAdapter: RepositoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        setupListeners()
        setupRetrofit()
        showUserName()
        setupRecyclerView()
    }

    private fun setupView() {
        nomeUsuario = findViewById(R.id.et_nome_usuario)
        btnConfirmar = findViewById(R.id.btn_confirmar)
        listaRepositories = findViewById(R.id.rv_lista_repositories)
        sharedPreferences = getSharedPreferences("GitHubSearch", MODE_PRIVATE)
    }

    private fun setupListeners() {
        btnConfirmar.setOnClickListener {
            saveUserLocal()
            getAllReposByUserName()
        }
    }

    private fun saveUserLocal() {
        val username = nomeUsuario.text.toString()
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.apply()
    }

    private fun showUserName() {
        val savedUsername = sharedPreferences.getString("username", "")
        nomeUsuario.setText(savedUsername)
    }

    private fun setupRetrofit() {

        githubApi = GitHubService.create()
    }

    private fun getAllReposByUserName() {
        val username = nomeUsuario.text.toString()
        if (username.isNotEmpty()) {
            val call = githubApi.getAllRepositoriesByUser(username)
            call.enqueue(object : Callback<List<Repository>> {
                override fun onResponse(call: Call<List<Repository>>, response: Response<List<Repository>>) {
                    if (response.isSuccessful) {
                        val repositories = response.body()
                        if (repositories != null) {
                            repositoryAdapter.submitList(repositories)
                        } else {

                        }
                    }
                }

                override fun onFailure(call: Call<List<Repository>>, t: Throwable) {

                }
            })
        }
    }

    private fun setupRecyclerView() {
        repositoryAdapter = RepositoryAdapter()
        repositoryAdapter.carItemListener = { repository -> openBrowser(repository.htmlUrl) }
        repositoryAdapter.btnShareListener = { urlRepository -> shareRepositoryLink(urlRepository) }
        listaRepositories.adapter = repositoryAdapter
    }

    fun shareRepositoryLink(urlRepository: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, urlRepository)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    fun openBrowser(urlRepository: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlRepository))
        startActivity(intent)
    }
}
